package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.BuildConfig
import com.example.capstoneproject.model.booking.Booking
import com.example.capstoneproject.model.booking.BookingDetail
import com.example.capstoneproject.network.ApiClient
import kotlinx.coroutines.launch

class BookingViewModel : ViewModel() {

    // List seluruh booking (ringkasan)
    var allBookings by mutableStateOf<List<Booking>>(emptyList())
        private set

    // List booking milik 1 customer aktif
    private val _userBookings = mutableStateOf<List<BookingDetail>>(emptyList())
    val userBookings: State<List<BookingDetail>> get() = _userBookings

    // Loading & error state global
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private val token: String
        get() = BuildConfig.API_ACCESS_TOKEN

    /**
     * Ambil semua booking dari endpoint /api/booking/all
     */
    fun fetchAllBookings() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = ApiClient.bookingService.getAllBookings(token)
                allBookings = response.data
            } catch (e: Exception) {
                errorMessage = "Gagal memuat data transaksi: ${e.localizedMessage ?: e.message}"
                allBookings = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Ambil booking milik satu customer dari /api/booking/detail/{customer_id}
     */
    fun fetchBookingsByCustomerId(customerId: String, accessToken: String = token) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = ApiClient.bookingService.getBookingsByCustomerId(customerId, accessToken)
                _userBookings.value = response
            } catch (e: Exception) {
                errorMessage = "Gagal memuat riwayat transaksi user: ${e.localizedMessage ?: e.message}"
                _userBookings.value = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Untuk menetapkan pesan error manual dari luar
     */
    fun setError(msg: String) {
        errorMessage = msg
    }
}