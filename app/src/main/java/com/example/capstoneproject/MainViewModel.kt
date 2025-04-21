package com.example.capstoneproject

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.Admin
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.network.ApiClient
import com.example.capstoneproject.util.Constants
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {

    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    private val _userRole = mutableStateOf<String?>(null)
    val userRole: State<String?> = _userRole

    private val _loginError = mutableStateOf<String?>(null)
    val loginError: State<String?> = _loginError

    var userName by mutableStateOf("")
        private set

    /**
     * Melakukan proses login berdasarkan data dari endpoint GET /api/admin/all
     */
    fun login(email: String, password: String, onResult: (Screen?) -> Unit) {
        viewModelScope.launch {
            try {
                val admins: List<Admin> = ApiClient.apiService.getAllAdmins(Constants.ACCESS_TOKEN)

                val user = admins.find {
                    it.admin_email.equals(email.trim(), ignoreCase = true) &&
                            it.admin_pass == password
                }

                if (user != null) {
                    _isLoggedIn.value = true
                    userName = user.admin_fullname

                    val destination = when (user.admin_who) {
                        2 -> {
                            _userRole.value = "root"
                            Screen.RootDashboard
                        }
                        1 -> {
                            _userRole.value = "admin"
                            Screen.AdminDashboard
                        }
                        else -> null
                    }

                    onResult(destination)
                } else {
                    _loginError.value = "Email atau password salah"
                    onResult(null)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _loginError.value = "Terjadi kesalahan saat login. Coba lagi nanti."
                onResult(null)
            }
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _userRole.value = null
        userName = ""
    }
}