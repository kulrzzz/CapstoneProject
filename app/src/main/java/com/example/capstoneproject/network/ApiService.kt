package com.example.capstoneproject.network

import com.example.capstoneproject.model.*
import retrofit2.http.*

interface ApiService {
    // ---------------- ROOM ----------------
    @GET("api/room/all")
    suspend fun getAllRooms(
        @Query("access_token") token: String
    ): RoomResponse

    // ---------------- ADMIN ----------------
    @GET("api/admin/all")
    suspend fun getAllAdmins(
        @Query("access_token") token: String
    ): List<Admin>

    // ---------------- CUSTOMER ----------------
    @GET("api/customer/all")
    suspend fun getAllCustomers(
        @Query("access_token") token: String
    ): CustomerResponse

    // ---------------- BOOKING ----------------
    @GET("api/booking/all")
    suspend fun getAllBookings(
        @Query("access_token") token: String
    ): BookingResponse
}