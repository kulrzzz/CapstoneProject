package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    fun setLoggedIn(value: Boolean) {
        _isLoggedIn.value = value
    }

    fun logout() {
        _isLoggedIn.value = false
    }
}