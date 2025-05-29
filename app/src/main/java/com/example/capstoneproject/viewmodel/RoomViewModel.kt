package com.example.capstoneproject.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.facility.*
import com.example.capstoneproject.model.room.*
import com.example.capstoneproject.network.ApiClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class RoomViewModel(private val token: String) : ViewModel() {

    private val _roomList = mutableStateListOf<Room>()
    val roomList: List<Room> get() = _roomList
    val accessToken: String get() = token

    private val _roomDetail = mutableStateOf<RoomWithDetails?>(null)
    val roomDetail: State<RoomWithDetails?> = _roomDetail

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _successMessage = mutableStateOf<String?>(null)
    val successMessage: State<String?> = _successMessage

    private val roomService by lazy { ApiClient.roomService }
    private val facilityService by lazy { ApiClient.facilityService }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    fun clearRoomDetail() {
        _roomDetail.value = null
    }

    fun fetchRooms(retryCount: Int = 3) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repeat(retryCount) { attempt ->
                try {
                    val result = roomService.getAllRooms(token)
                    _roomList.clear()
                    _roomList.addAll(result)
                    _isLoading.value = false
                    return@launch
                } catch (e: Exception) {
                    if (attempt == retryCount - 1) {
                        _errorMessage.value = "Gagal memuat data ruangan: ${e.localizedMessage}"
                    } else {
                        delay(500)
                    }
                }
            }

            _isLoading.value = false
        }
    }

    fun fetchRoomDetailById(roomId: String) {
        launchWithLoading {
            try {
                clearMessages()
                val detail = roomService.getRoomDetail(roomId, token)
                _roomDetail.value = detail ?: throw Exception("Null response")
            } catch (e: Exception) {
                setError("Gagal mengambil detail ruangan", e)
            }
        }
    }

    fun addRoomMultipart(
        room: Room,
        imageUris: List<Uri>,
        fasilitasList: List<String>,
        context: Context,
        onComplete: (Boolean) -> Unit
    ) {
        launchWithLoading {
            try {
                clearMessages()

                val payload = FullAddRoomPayload(
                    token = token,
                    room_name = room.room_name,
                    room_desc = room.room_desc,
                    room_kategori = room.room_kategori,
                    room_capacity = room.room_capacity,
                    room_price = room.room_price,
                    room_available = room.room_available,
                    room_start = room.room_start,
                    room_end = room.room_end,
                    facilities = fasilitasList.map { FacilityCreatePayload(it) },
                    imageFiles = imageUris.map { uri ->
                        val inputStream = context.contentResolver.openInputStream(uri)
                        val file = File.createTempFile("upload", ".jpg", context.cacheDir)
                        val outputStream = FileOutputStream(file)

                        inputStream?.use { input ->
                            outputStream.use { output ->
                                input.copyTo(output)
                            }
                        }

                        file // ✅ Return the file here
                    }
                )

                val (textParts, imageParts) = payload.toMultipartParts()

                val response = roomService.createRoomMultipart(
                    accessToken = textParts["access_token"]!!,
                    roomName = textParts["room_name"]!!,
                    roomDesc = textParts["room_desc"]!!,
                    roomKategori = textParts["room_kategori"]!!,
                    roomCapacity = textParts["room_capacity"]!!,
                    roomPrice = textParts["room_price"]!!,
                    roomAvailable = textParts["room_available"]!!,
                    roomStart = textParts["room_start"]!!,
                    roomEnd = textParts["room_end"]!!,
                    facilityJson = textParts["facility"]!!,
                    images = imageParts
                )

                _successMessage.value = "Ruangan berhasil ditambahkan"
                fetchRooms()
                onComplete(true)

            } catch (e: Exception) {
                setError("Gagal menambahkan ruangan", e)
                onComplete(false)
            }
        }
    }

    fun updateRoom(room: Room, onResult: (Boolean) -> Unit) {
        launchWithLoading {
            try {
                clearMessages()
                val request = RoomUpdateRequest(
                    access_token = token,
                    room_id = room.room_id,
                    room_name = room.room_name,
                    room_desc = room.room_desc,
                    room_kategori = room.room_kategori,
                    room_capacity = room.room_capacity,
                    room_price = room.room_price,
                    room_available = room.room_available,
                    room_start = room.room_start,
                    room_end = room.room_end
                )
                val result = roomService.updateRoom(request)

                if (result.isSuccessful) {
                    _successMessage.value = "Ruangan berhasil diperbarui"
                    fetchRooms()
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal update ruangan: ${result.message()}"
                    onResult(false)
                }
            } catch (e: Exception) {
                setError("Kesalahan saat update ruangan", e)
                onResult(false)
            }
        }
    }

    fun toggleRoomAvailability(room: Room, isAvailable: Boolean, onResult: (Boolean) -> Unit) {
        val updatedRoom = room.copy(room_available = if (isAvailable) 1 else 0)
        updateRoom(updatedRoom, onResult)
    }

    fun deleteRoomById(roomId: String, onResult: (Boolean) -> Unit) {
        launchWithLoading {
            try {
                clearMessages()
                val request = RoomDeleteRequest(token, roomId)
                val response = roomService.deleteRoom(request)

                if (response.isSuccessful) {
                    _roomList.removeAll { it.room_id == roomId }
                    _roomDetail.value = null
                    _successMessage.value = "Ruangan berhasil dihapus"
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal hapus ruangan: ${response.message()}"
                    onResult(false)
                }
            } catch (e: Exception) {
                setError("Kesalahan saat hapus ruangan", e)
                onResult(false)
            }
        }
    }

    fun addFacilityToRoom(request: FacilityCreateRequest, onResult: (Boolean) -> Unit) {
        launchWithLoading {
            try {
                clearMessages()
                val response = facilityService.addFacility(request)
                if (response.isSuccessful) {
                    _successMessage.value = "Fasilitas berhasil ditambahkan"
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal menambahkan fasilitas: ${response.message()}"
                    onResult(false)
                }
            } catch (e: Exception) {
                setError("Kesalahan saat menambahkan fasilitas", e)
                onResult(false)
            }
        }
    }

    fun deleteFacilityFromRoom(request: FacilityDeleteRequest, onResult: (Boolean) -> Unit) {
        launchWithLoading {
            try {
                clearMessages()
                val response = facilityService.deleteFacility(request)
                if (response.isSuccessful) {
                    _successMessage.value = "Fasilitas berhasil dihapus"
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal menghapus fasilitas: ${response.message()}"
                    onResult(false)
                }
            } catch (e: Exception) {
                setError("Kesalahan saat menghapus fasilitas", e)
                onResult(false)
            }
        }
    }

    private fun setError(message: String, e: Exception) {
        _errorMessage.value = "$message: ${e.localizedMessage ?: e.message}"
    }

    private fun launchWithLoading(block: suspend () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                block()
            } finally {
                _isLoading.value = false
            }
        }
    }
}