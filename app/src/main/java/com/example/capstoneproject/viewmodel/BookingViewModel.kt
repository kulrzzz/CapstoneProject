package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.BuildConfig
import com.example.capstoneproject.model.booking.Booking
import com.example.capstoneproject.model.booking.BookingDetail
import com.example.capstoneproject.model.booking.BookingRawItem
import com.example.capstoneproject.model.booking.BookingRiwayatItem
import com.example.capstoneproject.network.ApiClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class BookingViewModel : ViewModel() {

    // List semua booking (original)
    var allBookings by mutableStateOf<List<Booking>>(emptyList())
        private set

    // List booking milik user tertentu (dari /booking/customer/{id})
    private val _userBookings = mutableStateOf<List<BookingDetail>>(emptyList())
    val userBookings: State<List<BookingDetail>> get() = _userBookings

    // List hasil gabungan booking + customer + room
    var bookingRiwayatItems by mutableStateOf<List<BookingRiwayatItem>>(emptyList())
        private set

    // Loading state
    var isLoading by mutableStateOf(false)
        private set

    // Error message
    var errorMessage by mutableStateOf<String?>(null)
        private set

    private val token: String
        get() = BuildConfig.API_ACCESS_TOKEN

    /**
     * Ambil semua booking dari endpoint /api/booking/all dan gabungkan dengan nama room dan nama customer
     */
    fun fetchRiwayatTransaksi() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                // Paralel request untuk booking, customer, room
                val bookingsDeferred = async { ApiClient.bookingService.getAllBookings(token) }
                val customersDeferred = async { ApiClient.customerService.getAllCustomers(token) }
                val roomsDeferred = async { ApiClient.roomService.getAllRooms(token) }

                val bookingsResponse = bookingsDeferred.await()
                val customers = customersDeferred.await()
                val rooms = roomsDeferred.await()

                // Tidak perlu parsing manual!
                val bookings = bookingsResponse.body()
                if (bookings == null) {
                    errorMessage = "Gagal memuat bookings: ${bookingsResponse.message()}"
                    bookingRiwayatItems = emptyList()
                    return@launch
                }

                // Debug log jika cocokkan gagal
                bookingRiwayatItems = bookings.mapNotNull { booking ->
                    val customer = customers.find { it.customer_id == booking.customer_id }
                    val room = rooms.find { it.room_id == booking.room_id }

                    if (customer == null) {
                        println("⚠️ Tidak menemukan customer untuk booking ${booking.booking_code} dengan customer_id=${booking.customer_id}")
                    }
                    if (room == null) {
                        println("⚠️ Tidak menemukan room untuk booking ${booking.booking_code} dengan room_id=${booking.room_id}")
                    }

                    if (customer != null && room != null) {
                        BookingRiwayatItem(
                            booking_code = booking.booking_code,
                            customer_fullname = customer.customer_fullname,
                            room_name = room.room_name,
                            booking_date = booking.booking_date
                        )
                    } else null
                }

            } catch (e: Exception) {
                errorMessage = "Gagal memuat data transaksi: ${e.localizedMessage ?: e.toString()}"
                bookingRiwayatItems = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Ambil booking milik satu customer dari /api/booking/customer/{customer_id}
     */
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

    /**
     * Manual set error message
     */
    fun setError(msg: String) {
        errorMessage = msg
    }

    /**
     * Clear error message
     */
    fun clearError() {
        errorMessage = null
    }
}