package com.example.capstoneproject.network

import com.example.capstoneproject.model.booking.BookingDetail
import com.example.capstoneproject.model.booking.BookingResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookingService {
    @GET("api/booking/all")
    suspend fun getAllBookings(
        @Query("access_token") token: String
    ): BookingResponse

    @GET("api/booking/detail/{booking_id}")
    suspend fun getBookingsByCustomerId(
        @Path("booking_id") customerId: String,
        @Query("access_token") token: String
    ): List<BookingDetail>
}