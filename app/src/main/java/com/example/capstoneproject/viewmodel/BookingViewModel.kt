package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.BuildConfig
import com.example.capstoneproject.model.booking.Booking
import com.example.capstoneproject.network.ApiClient
import kotlinx.coroutines.launch

class BookingViewModel : ViewModel() {

    var allBookings by mutableStateOf<List<Booking>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private val token: String
        get() = BuildConfig.API_ACCESS_TOKEN

    fun fetchAllBookings() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = ApiClient.bookingService.getAllBookings(token)
                allBookings = response.data
            } catch (e: Exception) {
                errorMessage = "Gagal memuat data transaksi: ${e.localizedMessage ?: e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun getBookingsByCustomerId(customerId: String): List<Booking> {
        return allBookings.filter { it.customer_id == customerId }
    }

    fun setError(msg: String) {
        errorMessage = msg
    }
}
