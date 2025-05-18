package com.example.capstoneproject.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.Room
import com.example.capstoneproject.network.ApiClient
import com.example.capstoneproject.util.Constants
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class RuanganViewModel : ViewModel() {

    private val _roomList = mutableStateListOf<Room>()
    val roomList: List<Room> get() = _roomList

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _successMessage = mutableStateOf<String?>(null)
    val successMessage: State<String?> = _successMessage

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    private val textPlain = "text/plain".toMediaTypeOrNull()
    private val imageMediaType = "image/*".toMediaTypeOrNull()

    fun fetchRooms() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = ApiClient.apiService.getAllRooms(Constants.ACCESS_TOKEN)
                _roomList.clear()
                _roomList.addAll(response.data)
            } catch (e: Exception) {
                _errorMessage.value = "Gagal mengambil data ruangan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addFullRoom(
        room: Room,
        imageFile: File,
        fasilitasList: List<String>,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val roomFields = mapOf(
                    "room_name" to room.room_name,
                    "room_desc" to room.room_desc,
                    "room_kategori" to (room.room_kategori ?: ""),
                    "room_capacity" to room.room_capacity.toString(),
                    "room_price" to room.room_price.toString(),
                    "room_available" to "1",
                    "room_start" to (room.room_start ?: ""),
                    "room_end" to (room.room_end ?: "")
                )

                Log.d("RuanganViewModel", "Mengirim request addRoomForm...")
                val response = ApiClient.apiService.addRoomForm(roomFields, Constants.ACCESS_TOKEN)

                if (response.isSuccessful && response.body() != null) {
                    val roomId = response.body()!!.data.room_id
                    Log.d("RuanganViewModel", "Room berhasil ditambahkan dengan ID: $roomId")

                    // Delay singkat untuk memberi waktu backend menyimpan data
                    delay(1000)

                    val imageSuccess = uploadRoomImageSync(roomId, imageFile)
                    Log.d("RuanganViewModel", "Upload image success: $imageSuccess")

                    if (!imageSuccess) {
                        _errorMessage.value = "Room berhasil dibuat, tapi gagal upload gambar."
                        onComplete(false)
                        return@launch
                    }

                    val failedFacilities = uploadAllFacilities(roomId, fasilitasList)
                    Log.d("RuanganViewModel", "Fasilitas gagal: ${failedFacilities.joinToString()}")

                    if (failedFacilities.isEmpty()) {
                        _successMessage.value = "Ruangan, gambar & fasilitas berhasil ditambahkan."
                        fetchRooms()
                        onComplete(true)
                    } else {
                        _errorMessage.value = "Sebagian fasilitas gagal ditambahkan: ${failedFacilities.joinToString()}"
                        fetchRooms()
                        onComplete(false)
                    }

                } else {
                    _errorMessage.value = "Gagal menambahkan ruangan: ${response.code()} - ${response.message()}"
                    Log.e("RuanganViewModel", "addRoomForm response: ${response.errorBody()?.string()}")
                    onComplete(false)
                }

            } catch (e: Exception) {
                _errorMessage.value = "Terjadi kesalahan: ${e.message}"
                Log.e("RuanganViewModel", "Exception: ${e.message}")
                onComplete(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun uploadRoomImageSync(roomId: String, imageFile: File): Boolean = withContext(Dispatchers.IO) {
        try {
            Log.d("UploadImage", "Image path: ${imageFile.absolutePath}, exists: ${imageFile.exists()}, size: ${imageFile.length()}")

            val imageBody = RequestBody.create(imageMediaType, imageFile)
            val multipartImage = MultipartBody.Part.createFormData("ri_image", imageFile.name, imageBody)
            val roomIdBody = roomId.toRequestBody(textPlain)
            val tokenHeader = "Bearer ${Constants.ACCESS_TOKEN}"

            val response = ApiClient.apiService.addRoomImageMultipart(
                token = tokenHeader,
                image = multipartImage,
                roomId = roomIdBody
            )

            if (!response.isSuccessful) {
                Log.e("UploadRoomImage", "Gagal upload image: ${response.code()} - ${response.message()}")
                Log.e("UploadRoomImage", "Response body: ${response.errorBody()?.string()}")
            }

            return@withContext response.isSuccessful
        } catch (e: Exception) {
            Log.e("UploadRoomImage", "Exception: ${e.message}")
            return@withContext false
        }
    }

    private suspend fun uploadAllFacilities(roomId: String, fasilitasList: List<String>): List<String> = coroutineScope {
        val failed = mutableListOf<String>()

        val tasks = fasilitasList.map { fasilitasName ->
            async {
                val result = addFacilitySync(roomId, fasilitasName)
                if (!result) failed.add(fasilitasName)
            }
        }

        tasks.awaitAll()
        return@coroutineScope failed
    }

    private suspend fun addFacilitySync(roomId: String, facilityName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.addFacility(
                facilityName = facilityName,
                roomId = roomId,
                token = Constants.ACCESS_TOKEN
            )

            if (!response.isSuccessful) {
                Log.e("AddFacility", "Fasilitas '$facilityName' gagal: ${response.code()}")
                Log.e("AddFacility", "Error body: ${response.errorBody()?.string()}")
            }

            return@withContext response.isSuccessful
        } catch (e: Exception) {
            Log.e("AddFacility", "Exception fasilitas '$facilityName': ${e.message}")
            return@withContext false
        }
    }

    fun updateRoom(roomId: String, updatedFields: Map<String, String>, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = ApiClient.apiService.updateRoom(
                    updatedFields + ("room_id" to roomId),
                    Constants.ACCESS_TOKEN
                )
                if (result.isSuccessful) {
                    _successMessage.value = "Ruangan berhasil diperbarui."
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal update ruangan: ${result.message()}"
                    onResult(false)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Kesalahan saat update ruangan: ${e.message}"
                onResult(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteRoomById(roomId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = ApiClient.apiService.deleteRoom(
                    mapOf("room_id" to roomId),
                    Constants.ACCESS_TOKEN
                )
                if (result.isSuccessful) {
                    _roomList.removeAll { it.room_id == roomId }
                    _successMessage.value = "Ruangan berhasil dihapus."
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal menghapus ruangan: ${result.message()}"
                    onResult(false)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Kesalahan saat menghapus ruangan: ${e.message}"
                onResult(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun String.toRequestBody(mediaType: okhttp3.MediaType?) =
        RequestBody.create(mediaType, this)

    private fun Int.toRequestBody(mediaType: okhttp3.MediaType?) =
        this.toString().toRequestBody(mediaType)

    private fun Long.toRequestBody(mediaType: okhttp3.MediaType?) =
        this.toString().toRequestBody(mediaType)
}