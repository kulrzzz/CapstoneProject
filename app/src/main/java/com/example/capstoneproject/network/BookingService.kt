package com.example.capstoneproject.network

import com.example.capstoneproject.model.booking.BookingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookingService {
    @GET("api/booking/all")
    suspend fun getAllBookings(
        @Query("access_token") token: String
    ): BookingResponse
}