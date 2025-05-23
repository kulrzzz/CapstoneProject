package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.facility.FacilityCreatePayload
import com.example.capstoneproject.model.facility.FacilityCreateRequest
import com.example.capstoneproject.model.facility.FacilityDeleteRequest
import com.example.capstoneproject.model.room.*
import com.example.capstoneproject.network.ApiClient
import com.example.capstoneproject.network.FacilityService
import com.example.capstoneproject.network.RoomImageService
import com.example.capstoneproject.network.RoomService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class RoomViewModel(private val token: String) : ViewModel() {

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

    private val roomService by lazy { ApiClient.retrofit.create(RoomService::class.java) }
    private val roomImageService by lazy { ApiClient.retrofit.create(RoomImageService::class.java) }
    private val facilityService by lazy { ApiClient.retrofit.create(FacilityService::class.java) }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    // 🚪 Ambil daftar ruangan
    fun fetchRooms() {
        launchWithLoading {
            try {
                val response = roomService.getAllRooms(token)
                _roomList.apply {
                    clear()
                    addAll(response.data)
                }
            } catch (e: Exception) {
                setError("Gagal mengambil data ruangan", e)
            }
        }
    }

    // 🏷 Ambil detail ruangan
    fun fetchRoomDetailById(roomId: String) {
        launchWithLoading {
            try {
                _roomDetail.value = roomService.getRoomDetail(roomId, token)
            } catch (e: Exception) {
                setError("Gagal mengambil detail ruangan", e)
            }
        }
    }

    // ➕ Tambah ruangan + gambar + fasilitas
    fun addFullRoom(
        room: Room,
        imageUris: List<String>,
        fasilitasNames: List<String>,
        onComplete: (Boolean) -> Unit
    ) {
        val imagePayloads = imageUris.map { RoomImageCreatePayload(it) }
        val fasilitasPayloads = fasilitasNames.map { FacilityCreatePayload(it) }

        submitRoomWithExtras(room, fasilitasPayloads, imagePayloads, onComplete)
    }

    private fun submitRoomWithExtras(
        room: Room,
        fasilitas: List<FacilityCreatePayload>,
        images: List<RoomImageCreatePayload>,
        onComplete: (Boolean) -> Unit
    ) {
        launchWithLoading {
            try {
                val request = createRoomRequest(room, fasilitas, images)
                val response = roomService.createRoom(request)

                if (response.isSuccessful) {
                    _successMessage.value = "Ruangan berhasil ditambahkan"
                    fetchRooms()
                    onComplete(true)
                } else {
                    _errorMessage.value = "Gagal tambah ruangan: ${response.message()}"
                    onComplete(false)
                }
            } catch (e: Exception) {
                setError("Kesalahan saat menambahkan ruangan", e)
                onComplete(false)
            }
        }
    }

    // ✏️ Perbarui ruangan
    fun updateRoom(room: Room, onResult: (Boolean) -> Unit) {
        launchWithLoading {
            try {
                val request = room.toUpdateRequest(token)
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

    // 🔁 Ganti status tersedia / tidak
    fun toggleRoomAvailability(room: Room, isAvailable: Boolean, onResult: (Boolean) -> Unit) {
        val updatedRoom = room.copy(room_available = if (isAvailable) 1 else 0)
        updateRoom(updatedRoom, onResult)
    }

    // ❌ Hapus ruangan
    fun deleteRoomById(roomId: String, onResult: (Boolean) -> Unit) {
        launchWithLoading {
            try {
                val request = RoomDeleteRequest(token, roomId)
                val response = roomService.deleteRoom(request)

                if (response.isSuccessful) {
                    _roomList.removeAll { it.room_id == roomId }
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

    // ➕ Fasilitas
    fun addFacilityToRoom(request: FacilityCreateRequest, onResult: (Boolean) -> Unit) {
        launchWithLoading {
            try {
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

    // ❌ Fasilitas
    fun deleteFacilityFromRoom(request: FacilityDeleteRequest, onResult: (Boolean) -> Unit) {
        launchWithLoading {
            try {
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

    // 🖼 Upload gambar
    fun uploadRoomImage(
        imagePart: MultipartBody.Part,
        roomIdPart: MultipartBody.Part,
        tokenPart: MultipartBody.Part,
        onResult: (Boolean) -> Unit
    ) {
        launchWithLoading {
            try {
                val response = roomImageService.addRoomImageMultipart(imagePart, roomIdPart, tokenPart)
                if (response.isSuccessful) {
                    _successMessage.value = "Gambar berhasil diunggah"
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal unggah gambar: ${response.message()}"
                    onResult(false)
                }
            } catch (e: Exception) {
                setError("Kesalahan saat unggah gambar", e)
                onResult(false)
            }
        }
    }

    // ❌ Gambar
    fun deleteRoomImage(request: RoomImageDeleteRequest, onResult: (Boolean) -> Unit) {
        launchWithLoading {
            try {
                val response = roomImageService.deleteRoomImage(request)
                if (response.isSuccessful) {
                    _successMessage.value = "Gambar berhasil dihapus"
                    onResult(true)
                } else {
                    _errorMessage.value = "Gagal hapus gambar: ${response.message()}"
                    onResult(false)
                }
            } catch (e: Exception) {
                setError("Kesalahan saat hapus gambar", e)
                onResult(false)
            }
        }
    }

    // 🧩 Helper: mapping room → update request
    private fun Room.toUpdateRequest(token: String) = RoomUpdateRequest(
        access_token = token,
        room_id = room_id,
        room_name = room_name,
        room_desc = room_desc,
        room_kategori = room_kategori,
        room_capacity = room_capacity,
        room_price = room_price,
        room_available = room_available,
        room_start = room_start,
        room_end = room_end
    )

    // 🧩 Helper: create full request
    private fun createRoomRequest(
        room: Room,
        fasilitas: List<FacilityCreatePayload>,
        images: List<RoomImageCreatePayload>
    ) = RoomWithExtrasCreateRequest(
        access_token = token,
        room_name = room.room_name,
        room_desc = room.room_desc,
        room_kategori = room.room_kategori,
        room_capacity = room.room_capacity,
        room_price = room.room_price,
        room_available = room.room_available,
        room_start = room.room_start,
        room_end = room.room_end,
        facility = fasilitas,
        images = images
    )

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