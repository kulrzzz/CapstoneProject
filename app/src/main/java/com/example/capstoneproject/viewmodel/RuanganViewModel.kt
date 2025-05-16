package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.Room
import com.example.capstoneproject.network.ApiClient
import com.example.capstoneproject.util.Constants
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class RuanganViewModel : ViewModel() {

    private val _roomList = mutableStateListOf<Room>()
    val roomList: List<Room> get() = _roomList

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // Ambil semua data ruangan
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

    // Hapus ruangan berdasarkan ID
    fun deleteRoomById(roomId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val result = ApiClient.apiService.deleteRoom(mapOf("room_id" to roomId), Constants.ACCESS_TOKEN)
                if (result.isSuccessful) {
                    _roomList.removeAll { it.room_id == roomId }
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    // Update data ruangan
    fun updateRoom(roomId: String, updatedFields: Map<String, String>, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val result: Response<ResponseBody> = ApiClient.apiService.updateRoom(updatedFields + ("room_id" to roomId), Constants.ACCESS_TOKEN)
                onResult(result.isSuccessful)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}