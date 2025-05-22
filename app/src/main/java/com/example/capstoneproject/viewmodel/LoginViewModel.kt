package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.LoginResponse
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.network.ApiClient
import com.example.capstoneproject.util.Constants
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginError = mutableStateOf<String?>(null)
    val loginError: State<String?> = _loginError

    private val _isAuthenticating = mutableStateOf(false)
    val isAuthenticating: State<Boolean> = _isAuthenticating

    var userName by mutableStateOf("")
        private set

    var userRole by mutableStateOf<String?>(null)
        private set

    fun login(email: String, password: String, onResult: (Screen?) -> Unit) {
        viewModelScope.launch {
            _isAuthenticating.value = true
            _loginError.value = null

            try {
                val response: LoginResponse = ApiClient.apiService.loginAdmin(
                    Constants.ACCESS_TOKEN,
                    email.trim(),
                    password.trim()
                )

                if (response.status == "success") {
                    userName = response.admin.admin_fullname ?: "User"
                    userRole = when (response.admin.admin_who) {
                        2 -> "root"
                        1 -> "admin"
                        else -> null
                    }

                    if (userRole != null) {
                        onResult(Screen.Dashboard)
                    } else {
                        _loginError.value = "Role pengguna tidak dikenali."
                        onResult(null)
                    }
                } else {
                    _loginError.value = response.message
                    onResult(null)
                }

            } catch (e: Exception) {
                _loginError.value = "Terjadi kesalahan saat login: ${e.message}"
                onResult(null)
            } finally {
                _isAuthenticating.value = false
            }
        }
    }

    fun clearLoginState() {
        _loginError.value = null
        userRole = null
        userName = ""
    }
}