package com.example.capstoneproject.network

import com.example.capstoneproject.model.*
import retrofit2.http.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

interface ApiService {

    // ---------------- LOGIN ----------------
    @FormUrlEncoded
    @POST("api/login/admin")
    suspend fun loginAdmin(
        @Field("admin_email") email: String,
        @Field("admin_pass") password: String,
        @Field("access_token") token: String
    ): LoginResponse

//
//    // Fungsi di bawah ini hanya dibutuhkan jika kamu ingin membuat, edit, atau hapus transaksi booking
//
//    @POST("api/booking/add")
//    suspend fun createBooking(
//        @Body booking: Booking,
//        @Query("access_token") token: String
//    ): Response<ResponseBody>
//
//    @PUT("api/booking/update")
//    suspend fun updateBooking(
//        @Body updatedFields: Map<String, String>,
//        @Query("access_token") token: String
//    ): Response<ResponseBody>
//
//    @HTTP(method = "DELETE", path = "api/booking/delete", hasBody = true)
//    suspend fun deleteBooking(
//        @Body payload: Map<String, String>,
//        @Query("access_token") token: String
//    ): Response<ResponseBody>
}