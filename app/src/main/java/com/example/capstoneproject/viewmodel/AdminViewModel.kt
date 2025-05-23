package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.admin.*
import com.example.capstoneproject.network.ApiClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val _adminList = mutableStateListOf<Admin>()
    val adminList: SnapshotStateList<Admin> get() = _adminList

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> get() = _errorMessage

    // ============================================
    // 📥 FETCH ADMIN (token in query param)
    // ============================================
    fun fetchAdmins(token: String, retryCount: Int = 3) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repeat(retryCount) { attempt ->
                try {
                    val result = ApiClient.adminService.getAllAdmins(token)
                    _adminList.clear()
                    _adminList.addAll(result)
                    _isLoading.value = false
                    return@launch
                } catch (e: Exception) {
                    e.printStackTrace()
                    if (attempt == retryCount - 1) {
                        _errorMessage.value = "Gagal memuat data admin: ${e.localizedMessage}"
                    } else {
                        delay(500)
                    }
                }
            }

            _isLoading.value = false
        }
    }

    fun refreshAdmins(token: String) {
        fetchAdmins(token)
    }

    // ============================================
    // ➕ CREATE ADMIN (token in body)
    // ============================================
    fun createAdmin(request: AdminCreateRequest, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = ApiClient.adminService.createAdmin(request)
                onResult(response.isSuccessful)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }

    // ============================================
    // ❌ DELETE ADMIN (token in body)
    // ============================================
    fun deleteAdmin(adminId: String, token: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val request = AdminDeleteRequest(
                    admin_id = adminId,
                    access_token = token
                )

                val response = ApiClient.adminService.deleteAdmin(request)

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

    // ============================================
    // ✏️ UPDATE ADMIN PASSWORD (token in body)
    // ============================================
    fun updateAdminPassword(adminId: String, newPassword: String, token: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val request = AdminUpdateRequest(
                    access_token = token,
                    admin_id = adminId,
                    admin_fullname = null,
                    admin_email = null,
                    admin_pass = newPassword,
                    admin_who = null
                )

                val response = ApiClient.adminService.updateAdmin(request)

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