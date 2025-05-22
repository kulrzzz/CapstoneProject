package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.Admin
import com.example.capstoneproject.network.ApiClient
import com.example.capstoneproject.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class AdminViewModel : ViewModel() {

    // ============================
    // 🔄 DATA STATE MANAGEMENT
    // ============================

    // List admin yang diamati langsung oleh Compose
    private val _adminList = mutableStateListOf<Admin>()
    val adminList: SnapshotStateList<Admin> get() = _adminList

    // Loading status untuk UI indicator
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    // Error message yang bisa ditampilkan di UI
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> get() = _errorMessage


    // =======================================
    // 📥 FETCH ADMIN DARI API DENGAN RETRY
    // =======================================

    /**
     * Mengambil daftar admin dari server.
     * Akan mencoba ulang otomatis jika gagal, maksimal [retryCount] kali.
     */
    fun fetchAdmins(retryCount: Int = 3) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repeat(retryCount) { attempt ->
                try {
                    val result = ApiClient.adminService.getAllAdmins(Constants.ACCESS_TOKEN)

                    if (result.isNotEmpty()) {
                        _adminList.clear()
                        _adminList.addAll(result)
                        _isLoading.value = false
                        return@launch // Berhasil, keluar dari fungsi
                    } else {
                        throw Exception("Data admin kosong.")
                    }

                } catch (e: Exception) {
                    e.printStackTrace()

                    // Jika percobaan terakhir tetap gagal
                    if (attempt == retryCount - 1) {
                        _errorMessage.value = "Gagal memuat data admin: ${e.localizedMessage}"
                    } else {
                        delay(500) // Jeda retry
                    }
                }
            }

            _isLoading.value = false
        }
    }

    /**
     * Dipanggil saat user ingin refresh ulang data
     */
    fun refreshAdmins() {
        fetchAdmins()
    }

    // ============================
    // ❌ DELETE ADMIN
    // ============================

    /**
     * Menghapus admin berdasarkan ID.
     * Jika sukses, data lokal juga diperbarui.
     */
    fun deleteAdmin(adminId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val payload = mapOf("admin_id" to adminId)
                val response = ApiClient.adminService.deleteAdmin(payload, Constants.ACCESS_TOKEN)

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

    // ============================
    // 🔑 UPDATE PASSWORD ADMIN
    // ============================

    /**
     * Mengupdate password admin tertentu.
     */
    fun updateAdminPassword(adminId: String, newPassword: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val payload = mapOf(
                    "admin_id" to adminId,
                    "admin_pass" to newPassword
                )

                val response: Response<ResponseBody> =
                    ApiClient.adminService.updateAdmin(payload, Constants.ACCESS_TOKEN)

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

    // ============================
    // 🚫 CLEAR ERROR STATE
    // ============================

    /**
     * Menghapus error saat ini (biasa dipakai sebelum loading baru dimulai).
     */
    fun clearError() {
        _errorMessage.value = null
    }
}