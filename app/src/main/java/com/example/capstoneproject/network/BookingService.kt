package com.example.capstoneproject.network

import com.example.capstoneproject.model.booking.Booking
import com.example.capstoneproject.model.booking.CustomerBookingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface BookingService {

    @Streaming
    @GET("api/booking/all-mini")
    suspend fun getAllBookings(
        @Query("access_token") token: String
    ): Response<List<Booking>>

    @GET("api/booking/customer/{customer_id}")
    suspend fun getBookingsByCustomerId(
        @Path("customer_id") customerId: String,
        @Query("access_token") token: String
    ): CustomerBookingResponse
}