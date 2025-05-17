package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.*
import com.example.capstoneproject.network.ApiClient
import com.example.capstoneproject.util.Constants
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

class RuanganViewModel : ViewModel() {

    // -------------------- STATE --------------------
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

    // -------------------- FETCH --------------------
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

    // -------------------- ADD ROOM --------------------
    fun addRoomMultipart(room: Room, imageFile: File, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val parts = mutableMapOf<String, RequestBody>(
                    "room_name" to room.room_name.toRequestBody(textPlain),
                    "room_desc" to room.room_desc.toRequestBody(textPlain),
                    "room_kategori" to (room.room_kategori ?: "").toRequestBody(textPlain),
                    "room_capacity" to room.room_capacity.toRequestBody(textPlain),
                    "room_price" to room.room_price.toRequestBody(textPlain),
                    "room_available" to room.room_available.toRequestBody(textPlain),
                    "room_start" to (room.room_start ?: "").toRequestBody(textPlain),
                    "room_end" to (room.room_end ?: "").toRequestBody(textPlain)
                )

                val imageRequest = RequestBody.create(imageMediaType, imageFile)
                val multipartImage = MultipartBody.Part.createFormData("image", imageFile.name, imageRequest)

                val response = ApiClient.apiService.addRoomMultipart(parts, multipartImage, Constants.ACCESS_TOKEN)
                if (response.isSuccessful) {
                    _successMessage.value = "Ruangan berhasil ditambahkan."
                    fetchRooms()
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal menambahkan ruangan: ${response.code()} ${response.message()}"
                    onResult(false)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Terjadi kesalahan saat menambahkan ruangan: ${e.message}"
                onResult(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // -------------------- UPLOAD IMAGE --------------------
    fun uploadRoomImage(roomId: String, imageFile: File, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val imageBody = RequestBody.create(imageMediaType, imageFile)
                val multipartImage = MultipartBody.Part.createFormData("ri_image", imageFile.name, imageBody)
                val roomIdBody = roomId.toRequestBody(textPlain)

                val response = ApiClient.apiService.addRoomImageMultipart(multipartImage, roomIdBody, Constants.ACCESS_TOKEN)
                if (response.isSuccessful) {
                    _successMessage.value = "Gambar berhasil diunggah."
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal upload gambar: ${response.code()} ${response.message()}"
                    onResult(false)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Kesalahan upload gambar: ${e.message}"
                onResult(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // -------------------- ADD FACILITY --------------------
    fun addFacility(facility: Facility, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiClient.apiService.addFacility(facility, Constants.ACCESS_TOKEN)
                if (response.isSuccessful) {
                    _successMessage.value = "Fasilitas berhasil ditambahkan."
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal menambahkan fasilitas: ${response.message()}"
                    onResult(false)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Kesalahan saat menambahkan fasilitas: ${e.message}"
                onResult(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // -------------------- UPDATE ROOM --------------------
    fun updateRoom(roomId: String, updatedFields: Map<String, String>, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = ApiClient.apiService.updateRoom(updatedFields + ("room_id" to roomId), Constants.ACCESS_TOKEN)
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

    // -------------------- DELETE ROOM --------------------
    fun deleteRoomById(roomId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = ApiClient.apiService.deleteRoom(mapOf("room_id" to roomId), Constants.ACCESS_TOKEN)
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

    // -------------------- UTIL EXTENSION --------------------
    private fun String.toRequestBody(mediaType: okhttp3.MediaType?) =
        RequestBody.create(mediaType, this)

    private fun Int.toRequestBody(mediaType: okhttp3.MediaType?) =
        this.toString().toRequestBody(mediaType)

    private fun Long.toRequestBody(mediaType: okhttp3.MediaType?) =
        this.toString().toRequestBody(mediaType)
}