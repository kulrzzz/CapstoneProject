package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.Booking
import com.example.capstoneproject.network.ApiClient
import com.example.capstoneproject.util.Constants
import kotlinx.coroutines.launch

class BookingViewModel : ViewModel() {

    var allBookings by mutableStateOf<List<Booking>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchAllBookings() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = ApiClient.apiService.getAllBookings(Constants.ACCESS_TOKEN)
                allBookings = response.data
            } catch (e: Exception) {
                errorMessage = "Gagal memuat data transaksi: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}