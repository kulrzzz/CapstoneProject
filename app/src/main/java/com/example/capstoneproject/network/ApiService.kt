package com.example.capstoneproject.network

import com.example.capstoneproject.model.Admin
import com.example.capstoneproject.model.BookingResponse
import com.example.capstoneproject.model.CustomerResponse
import com.example.capstoneproject.model.RoomResponse
import okhttp3.ResponseBody
import retrofit2.Response
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

    @POST("api/admin/add")
    suspend fun createAdmin(
        @Body admin: Admin,
        @Query("access_token") token: String
    ): Response<ResponseBody>

    @PUT("api/admin/update")
    suspend fun updateAdmin(
        @Body updatedFields: Map<String, String>,
        @Query("access_token") token: String
    ): Response<ResponseBody>

    @HTTP(method = "DELETE", path = "api/admin/delete", hasBody = true)
    suspend fun deleteAdmin(
        @Body payload: Map<String, String>,
        @Query("access_token") token: String
    ): Response<ResponseBody>

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