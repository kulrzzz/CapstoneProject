package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.BuildConfig
import com.example.capstoneproject.model.Booking
import com.example.capstoneproject.network.ApiClient
import kotlinx.coroutines.launch

class BookingViewModel : ViewModel() {

    var allBookings by mutableStateOf<List<Booking>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Mendapatkan token akses.
     * Bisa diganti runtime jika kamu ingin inject token dinamis dari login.
     */
    private val token: String
        get() = BuildConfig.API_ACCESS_TOKEN

    /**
     * Fetch semua data transaksi dari server.
     */
    fun fetchAllBookings() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = ApiClient.apiService.getAllBookings("Bearer $token")
                allBookings = response.data
            } catch (e: Exception) {
                errorMessage = "Gagal memuat data transaksi: ${e.localizedMessage ?: e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}