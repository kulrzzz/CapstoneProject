package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.BuildConfig
import com.example.capstoneproject.model.booking.Booking
import com.example.capstoneproject.model.booking.BookingDetail
import com.example.capstoneproject.network.ApiClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookingViewModel : ViewModel() {

    var allBookings by mutableStateOf<List<Booking>>(emptyList())
        private set

    private val _userBookings = mutableStateOf<List<BookingDetail>>(emptyList())
    val userBookings: State<List<BookingDetail>> get() = _userBookings

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)

    private val token: String
        get() = BuildConfig.API_ACCESS_TOKEN

    fun fetchRiwayatTransaksi(token: String, retryCount: Int = 3) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            repeat(retryCount) { attempt ->
                try {
                    println("🔁 Fetch attempt ke-${attempt + 1}")

                    // Ambil data langsung dari API yang sudah return List<Booking>
                    val response = ApiClient.bookingService.getAllBookings(token)

                    if (!response.isSuccessful || response.body() == null) {
                        throw Exception("Respon tidak sukses: ${response.code()} - ${response.message()}")
                    }

                    val bookings = response.body()!!
                    println("✅ Total bookings: ${bookings.size}")

                    allBookings = bookings
                    isLoading = false
                    return@launch

                } catch (e: Exception) {
                    e.printStackTrace()
                    println("❌ Gagal di attempt ke-${attempt + 1}: ${e.localizedMessage}")

                    if (attempt == retryCount - 1) {
                        errorMessage = "Gagal memuat data transaksi: ${e.localizedMessage ?: e.message}"
                        allBookings = emptyList()
                    } else {
                        delay(800)
                    }
                }
            }

            isLoading = false
        }
    }

    fun fetchBookingsByCustomerId(customerId: String, accessToken: String = token) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = ApiClient.bookingService.getBookingsByCustomerId(customerId, accessToken)
                _userBookings.value = response.booking
            } catch (e: Exception) {
                errorMessage = "Gagal memuat transaksi user: ${e.localizedMessage ?: e.message}"
                _userBookings.value = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    fun setError(msg: String) {
        errorMessage = msg
    }

    fun clearError() {
        errorMessage = null
    }
}