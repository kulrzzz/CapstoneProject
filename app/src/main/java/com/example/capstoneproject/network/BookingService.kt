package com.example.capstoneproject.network

import com.example.capstoneproject.model.booking.BookingRawItem
import com.example.capstoneproject.model.booking.CustomerBookingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookingService {
    @GET("api/booking/all")
    suspend fun getAllBookings(
        @Query("access_token") token: String
    ): Response<List<BookingRawItem>>

    @GET("api/booking/customer/{customer_id}")
    suspend fun getBookingsByCustomerId(
        @Path("customer_id") customerId: String,
        @Query("access_token") token: String
    ): CustomerBookingResponse
}