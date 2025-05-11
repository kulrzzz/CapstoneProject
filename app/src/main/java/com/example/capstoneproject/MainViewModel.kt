package com.example.capstoneproject

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.Admin
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.network.ApiClient
import com.example.capstoneproject.util.Constants
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    private val _userRole = mutableStateOf<String?>(null)
    val userRole: State<String?> = _userRole

    private val _loginError = mutableStateOf<String?>(null)
    val loginError: State<String?> = _loginError

    var userName by mutableStateOf("")
        private set

    fun login(email: String, password: String, onResult: (Screen?) -> Unit) {
        viewModelScope.launch {
            try {
                val trimmedEmail = email.trim()
                val trimmedPassword = password.trim()

                // 🔥 Dummy Account: root
                if (trimmedEmail == "root" && trimmedPassword == "root") {
                    _isLoggedIn.value = true
                    userName = "Root User"
                    _userRole.value = "root"
                    onResult(Screen.Dashboard)
                    return@launch
                }

                // 🔍 Pengecekan di API jika bukan akun dummy
                val admins: List<Admin> = ApiClient.apiService.getAllAdmins(Constants.ACCESS_TOKEN)

                val user = admins.find {
                    it.admin_email?.trim()?.equals(trimmedEmail, ignoreCase = true) == true &&
                            it.admin_pass?.trim() == trimmedPassword
                }

                if (user != null) {
                    _isLoggedIn.value = true
                    userName = user.admin_fullname ?: "User"

                    _userRole.value = when (user.admin_who) {
                        2 -> "root"
                        1 -> "admin"
                        else -> null
                    }

                    if (_userRole.value != null) {
                        onResult(Screen.Dashboard)
                    } else {
                        _loginError.value = "Role pengguna tidak dikenali."
                        onResult(null)
                    }

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
