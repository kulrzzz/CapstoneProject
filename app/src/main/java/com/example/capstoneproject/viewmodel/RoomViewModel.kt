package com.example.capstoneproject.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.facility.*
import com.example.capstoneproject.model.room.*
import com.example.capstoneproject.network.ApiClient
import com.example.capstoneproject.network.FacilityService
import com.example.capstoneproject.network.RoomImageService
import com.example.capstoneproject.network.RoomService
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
    private val roomImageService by lazy { ApiClient.roomImageService }
    private val facilityService by lazy { ApiClient.facilityService }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    fun clearRoomDetail() {
        _roomDetail.value = null
    }

    fun fetchRooms() {
        launchWithLoading {
            try {
                clearMessages()
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

    fun fetchRoomDetailById(roomId: String) {
        launchWithLoading {
            try {
                clearMessages()
                val detail = roomService.getRoomDetail(roomId, token)
                if (detail != null) {
                    _roomDetail.value = detail
                } else {
                    setError("Data ruangan tidak ditemukan", Exception("Null response"))
                }
            } catch (e: Exception) {
                setError("Gagal mengambil detail ruangan", e)
            }
        }
    }

    fun addRoomWithImageAndFacilities(
        room: Room,
        imageUri: Uri?,
        context: Context,
        fasilitasList: List<String>,
        onComplete: (Boolean) -> Unit
    ) {
        launchWithLoading {
            try {
                clearMessages()
                val request = createRoomRequest(room, emptyList(), emptyList())
                val response = roomService.createRoom(request)

                if (response.status == "success") {
                    val roomId = response.data?.room_id
                    _successMessage.value = "Ruangan berhasil ditambahkan"
                    fetchRooms()

                    if (roomId != null && imageUri != null) {
                        println("🟢 Room ID created: $roomId")
                        val imagePart = uriToMultipart(imageUri, context)
                        val roomIdPart = createPartFromString(roomId)
                        val tokenPart = getTokenPart()

                        uploadRoomImageSafe(imagePart, roomIdPart, tokenPart) { imageSuccess ->
                            if (!imageSuccess) {
                                println("❌ Gagal upload gambar")
                            }
                        }
                    }

                    fasilitasList.forEach { fasilitas ->
                        val request = FacilityCreateRequest(
                            access_token = token,
                            room_id = roomId!!,
                            facility_name = fasilitas
                        )
                        addFacilityToRoom(request) { success ->
                            if (!success) {
                                println("❌ Gagal tambah fasilitas: $fasilitas")
                            }
                        }
                    }

                    onComplete(true)
                } else {
                    _errorMessage.value = "Gagal tambah ruangan"
                    onComplete(false)
                }
            } catch (e: Exception) {
                setError("Kesalahan saat menambahkan ruangan", e)
                onComplete(false)
            }
        }
    }

    fun updateRoom(room: Room, onResult: (Boolean) -> Unit) {
        launchWithLoading {
            try {
                clearMessages()
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

    fun uploadRoomImageSafe(
        imagePart: MultipartBody.Part,
        roomIdPart: MultipartBody.Part,
        tokenPart: MultipartBody.Part,
        onResult: (Boolean) -> Unit
    ) {
        launchWithLoading {
            try {
                clearMessages()
                println("🔥 Uploading image to server...")
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

    fun deleteRoomImage(request: RoomImageDeleteRequest, onResult: (Boolean) -> Unit) {
        launchWithLoading {
            try {
                clearMessages()
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

    // 🔧 Helper: Buat multipart dari Uri
    fun uriToMultipart(uri: Uri, context: Context): MultipartBody.Part {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File.createTempFile("upload", ".jpg", context.cacheDir)
        val outputStream = FileOutputStream(file)
        inputStream?.use { input -> outputStream.use { output -> input.copyTo(output) } }
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("ri_image", file.name, requestFile)
    }

    fun createPartFromString(value: String): MultipartBody.Part {
        val requestBody = value.toRequestBody("text/plain".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("room_id", null, requestBody)
    }

    fun getTokenPart(): MultipartBody.Part {
        val tokenBody = token.toRequestBody("text/plain".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("access_token", null, tokenBody)
    }

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