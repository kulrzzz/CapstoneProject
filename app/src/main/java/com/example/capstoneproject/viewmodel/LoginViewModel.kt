package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.BuildConfig
import com.example.capstoneproject.model.LoginResponse
import com.example.capstoneproject.model.admin.Admin
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.network.ApiClient
import com.example.capstoneproject.util.TokenProvider
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginError = mutableStateOf<String?>(null)
    val loginError: State<String?> get() = _loginError

    private val _isAuthenticating = mutableStateOf(false)
    val isAuthenticating: State<Boolean> get() = _isAuthenticating

    var userName by mutableStateOf("")
        private set

    var userRole by mutableStateOf<String?>(null)
        private set

    var token by mutableStateOf<String?>(null)
        private set

    var admin by mutableStateOf<Admin?>(null)
        private set

    val effectiveToken: String
        get() = token ?: TokenProvider.accessToken.orEmpty()

    fun login(email: String, password: String, onResult: (Screen?) -> Unit) {
        viewModelScope.launch {
            _isAuthenticating.value = true
            _loginError.value = null

            try {
                val apiToken = BuildConfig.API_ACCESS_TOKEN

                val response: LoginResponse = ApiClient.apiService.loginAdmin(
                    email = email.trim(),
                    password = password.trim(),
                    token = apiToken
                )

                if (response.status == "success" && response.admin != null && !response.role.isNullOrBlank()) {
                    // Set token dan informasi user
                    token = apiToken
                    TokenProvider.accessToken = apiToken

                    admin = response.admin
                    userName = response.admin.admin_fullname ?: "User"

                    // Normalisasi role
                    userRole = when (response.role.lowercase()) {
                        "admin", "1" -> "admin"
                        "superadmin", "2" -> "superadmin"
                        else -> response.role.lowercase()
                    }

                    // Navigasi berdasarkan role
                    when (userRole) {
                        "admin", "superadmin" -> onResult(Screen.Dashboard)
                        else -> {
                            _loginError.value = "Role pengguna tidak dikenali."
                            onResult(null)
                        }
                    }
                } else {
                    _loginError.value = response.message ?: "Login gagal."
                    onResult(null)
                }

            } catch (e: Exception) {
                _loginError.value = "Terjadi kesalahan saat login: ${e.localizedMessage ?: e.message}"
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
        token = null
        admin = null
        TokenProvider.accessToken = null
    }
}