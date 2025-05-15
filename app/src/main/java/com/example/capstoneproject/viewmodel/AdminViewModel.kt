package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.Admin
import com.example.capstoneproject.network.ApiClient
import com.example.capstoneproject.util.Constants
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class AdminViewModel : ViewModel() {

    private val _adminList = mutableStateListOf<Admin>()
    val adminList: List<Admin> get() = _adminList

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun fetchAdmins() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = ApiClient.apiService.getAllAdmins(Constants.ACCESS_TOKEN)
                _adminList.clear()
                _adminList.addAll(result)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Gagal memuat data admin: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAdmin(adminId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val payload = mapOf("admin_id" to adminId)
                val response = ApiClient.apiService.deleteAdmin(payload, Constants.ACCESS_TOKEN)

                if (response.isSuccessful) {
                    _adminList.removeAll { it.admin_id == adminId }
                    onResult(true)
                } else {
                    println("Delete failed: ${response.code()} - ${response.errorBody()?.string()}")
                    onResult(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }

    fun updateAdminPassword(adminId: String, newPassword: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val payload = mapOf(
                    "admin_id" to adminId,
                    "admin_pass" to newPassword
                )

                val response: Response<ResponseBody> =
                    ApiClient.apiService.updateAdmin(payload, Constants.ACCESS_TOKEN)

                if (!response.isSuccessful) {
                    val errorBody = response.errorBody()?.string()
                    println("Update failed: ${response.code()} → $errorBody")
                }

                onResult(response.isSuccessful)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}