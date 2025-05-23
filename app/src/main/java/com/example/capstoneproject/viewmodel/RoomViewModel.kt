package com.example.capstoneproject.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.Room
import com.example.capstoneproject.model.RoomWithDetails
import com.example.capstoneproject.model.RoomSingleResponse
import com.example.capstoneproject.model.FacilityResponse
import com.example.capstoneproject.network.ApiClient
import com.example.capstoneproject.network.FacilityService
import com.example.capstoneproject.network.RoomImageService
import com.example.capstoneproject.network.RoomService
import com.example.capstoneproject.util.Constants
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class RoomViewModel : ViewModel() {

    private val _roomList = mutableStateListOf<Room>()
    val roomList: List<Room> get() = _roomList

    private val _roomDetail = mutableStateOf<RoomWithDetails?>(null)
    val roomDetail: State<RoomWithDetails?> = _roomDetail

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

    private val roomService = ApiClient.getClientWithAuth(Constants.ACCESS_TOKEN).create(RoomService::class.java)
    private val roomImageService = ApiClient.getClientWithAuth(Constants.ACCESS_TOKEN).create(RoomImageService::class.java)
    private val facilityService = ApiClient.getClientWithAuth(Constants.ACCESS_TOKEN).create(FacilityService::class.java)

    fun fetchRooms() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = roomService.getAllRooms(Constants.ACCESS_TOKEN)
                _roomList.clear()
                _roomList.addAll(response.data)
            } catch (e: Exception) {
                _errorMessage.value = "Gagal mengambil data ruangan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchRoomDetailById(roomId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val detail = roomService.getRoomDetail(roomId, Constants.ACCESS_TOKEN)
                _roomDetail.value = detail
            } catch (e: Exception) {
                _errorMessage.value = "Gagal mengambil detail ruangan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addFullRoom(
        context: Context,
        room: Room,
        imageUri: Uri?,
        fasilitasList: List<String>,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            if (Constants.ACCESS_TOKEN.isBlank()) {
                _errorMessage.value = "Token tidak valid. Mohon login ulang."
                _isLoading.value = false
                onComplete(false)
                return@launch
            }

            try {
                val roomFields = mapOf(
                    "room_name" to room.room_name,
                    "room_desc" to room.room_desc,
                    "room_kategori" to room.room_kategori,
                    "room_capacity" to room.room_capacity.toString(),
                    "room_price" to room.room_price.toString(),
                    "room_available" to room.room_available.toString(),
                    "room_start" to room.room_start,
                    "room_end" to room.room_end
                )

                val response = roomService.addRoomForm(roomFields, Constants.ACCESS_TOKEN)

                if (response.isSuccessful && response.body() != null) {
                    val roomId = response.body()?.data?.room_id
                    if (roomId.isNullOrEmpty()) {
                        _errorMessage.value = "room_id tidak tersedia dari server"
                        onComplete(false)
                        return@launch
                    }
                    delay(1000)

                    val imageSuccess = if (imageUri != null) uploadRoomImageFromUri(context, roomId, imageUri) else true
                    val failedFacilities = uploadAllFacilities(roomId, fasilitasList)

                    if (!imageSuccess) {
                        _errorMessage.value = "Gambar gagal diupload."
                        onComplete(false)
                        return@launch
                    }

                    if (failedFacilities.isEmpty()) {
                        _successMessage.value = "Ruangan berhasil ditambahkan."
                        fetchRooms()
                        onComplete(true)
                    } else {
                        _errorMessage.value = "Sebagian fasilitas gagal ditambahkan: ${failedFacilities.joinToString()}"
                        fetchRooms()
                        onComplete(false)
                    }

                } else {
                    _errorMessage.value = "Gagal menambahkan ruangan: ${response.code()} - ${response.message()}"
                    onComplete(false)
                }

            } catch (e: Exception) {
                _errorMessage.value = "Kesalahan: ${e.message}"
                onComplete(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun uploadRoomImageFromUri(context: Context, roomId: String, uri: Uri): Boolean = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext false
            val file = File(context.cacheDir, "upload_image.jpg")
            file.outputStream().use { inputStream.copyTo(it) }

            val imageBody = file.asRequestBody(imageMediaType)
            val multipartImage = MultipartBody.Part.createFormData("ri_image", file.name, imageBody)
            val roomIdBody = roomId.toRequestBody(textPlain)
            val response = roomImageService.addRoomImageMultipart("Bearer ${Constants.ACCESS_TOKEN}", multipartImage, roomIdBody)
            return@withContext response.isSuccessful
        } catch (e: Exception) {
            Log.e("UploadImageUri", "Upload gagal: ${e.message}")
            return@withContext false
        }
    }

    private suspend fun uploadAllFacilities(roomId: String, fasilitasList: List<String>): List<String> = coroutineScope {
        val failed = mutableListOf<String>()
        val jobs = fasilitasList.map { name ->
            async {
                val success = addFacilitySync(roomId, name)
                if (!success) failed.add(name)
            }
        }
        jobs.awaitAll()
        return@coroutineScope failed
    }

    private suspend fun addFacilitySync(roomId: String, facilityName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val result = facilityService.addFacility(facilityName, roomId, Constants.ACCESS_TOKEN)
            if (result.isSuccessful) {
                Log.d("FacilityResponse", "facility_id = ${result.body()?.data?.facility_id}")
            } else {
                Log.e("FacilityResponse", "error = ${result.errorBody()?.string()}")
            }
            return@withContext result.isSuccessful
        } catch (e: Exception) {
            Log.e("AddFacilitySync", "Exception: ${e.message}")
            return@withContext false
        }
    }

    // ================== UPDATE ==================
    fun updateRoom(roomId: String, updatedRoom: Room, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val updatedFields = mapOf(
                    "room_id" to roomId,
                    "room_name" to updatedRoom.room_name,
                    "room_desc" to updatedRoom.room_desc,
                    "room_kategori" to updatedRoom.room_kategori,
                    "room_capacity" to updatedRoom.room_capacity.toString(),
                    "room_price" to updatedRoom.room_price.toString(),
                    "room_start" to updatedRoom.room_start,
                    "room_end" to updatedRoom.room_end,
                    "room_available" to updatedRoom.room_available.toString()
                )

                val result = roomService.updateRoom(updatedFields, Constants.ACCESS_TOKEN)
                if (result.isSuccessful) {
                    _successMessage.value = "Ruangan berhasil diperbarui."
                    fetchRooms()
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

    fun toggleRoomAvailability(room: Room, isAvailable: Boolean, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val updatedFields = mapOf(
                    "room_id" to room.room_id,
                    "room_available" to if (isAvailable) "1" else "0"
                )

                val result = roomService.updateRoom(updatedFields, Constants.ACCESS_TOKEN)
                if (result.isSuccessful) {
                    _successMessage.value = "Status ruangan diperbarui."
                    fetchRooms()
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal memperbarui status: ${result.message()}"
                    onResult(false)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Kesalahan saat ubah status ruangan: ${e.message}"
                onResult(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ================== DELETE ==================
    fun deleteRoomById(roomId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = roomService.deleteRoom(mapOf("room_id" to roomId), Constants.ACCESS_TOKEN)
                if (result.isSuccessful) {
                    _roomList.removeAll { it.room_id == roomId }
                    _successMessage.value = "Ruangan berhasil dihapus."
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal hapus ruangan: ${result.message()}"
                    onResult(false)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Kesalahan saat hapus ruangan: ${e.message}"
                onResult(false)
            } finally {
                _isLoading.value = false
            }
        }
    }
}