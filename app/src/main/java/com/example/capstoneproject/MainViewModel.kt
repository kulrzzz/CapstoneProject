package com.example.capstoneproject

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.capstoneproject.navigation.Screen

class MainViewModel : ViewModel() {

    // Login status
    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    // Role user (admin / root)
    private val _userRole = mutableStateOf<String?>(null)
    val userRole: State<String?> = _userRole

    // Fungsi login
    fun login(username: String, password: String): Screen? {
        return when {
            username == "admin" && password == "1234" -> {
                _isLoggedIn.value = true
                _userRole.value = "admin"
                Screen.AdminDashboard
            }
            username == "root" && password == "1234" -> {
                _isLoggedIn.value = true
                _userRole.value = "root"
                Screen.RootDashboard
            }
            else -> null
        }
    }

    // Fungsi logout
    fun logout() {
        _isLoggedIn.value = false
        _userRole.value = null
    }
}