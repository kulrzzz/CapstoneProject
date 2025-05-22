package com.example.capstoneproject.network

import com.example.capstoneproject.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ---------------- LOGIN ----------------
    @FormUrlEncoded
    @POST("api/login/admin")
    suspend fun loginAdmin(
        @Field("access_token") token: String,
        @Field("admin_email") email: String,
        @Field("admin_pass") password: String
    ): LoginResponse

    // ---------------- ROOM ----------------
    @GET("api/room/all")
    suspend fun getAllRooms(
        @Query("access_token") token: String
    ): RoomResponse

    @FormUrlEncoded
    @POST("api/room/add")
    suspend fun addRoomForm(
        @FieldMap roomFields: Map<String, String>,
        @Query("access_token") token: String
    ): Response<RoomAddResponse>

    @FormUrlEncoded
    @PUT("api/room/update")
    suspend fun updateRoom(
        @FieldMap updatedFields: Map<String, String>,
        @Query("access_token") token: String
    ): Response<ResponseBody>

    @HTTP(method = "DELETE", path = "api/room/delete", hasBody = true)
    suspend fun deleteRoom(
        @Body payload: Map<String, String>,
        @Query("access_token") token: String
    ): Response<ResponseBody>

    // ---------------- ROOM IMAGE ----------------
    @Multipart
    @POST("api/room-image/add")
    suspend fun addRoomImageMultipart(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("room_id") roomId: RequestBody
    ): Response<ResponseBody>

    @HTTP(method = "DELETE", path = "api/room/image/delete", hasBody = true)
    suspend fun deleteRoomImage(
        @Body payload: Map<String, String>,
        @Query("access_token") token: String
    ): Response<ResponseBody>

    // ---------------- FACILITY ----------------
    @FormUrlEncoded
    @POST("api/room-facility/add")
    suspend fun addFacility(
        @Field("facility_name") facilityName: String,
        @Field("room_id") roomId: String,
        @Query("access_token") token: String
    ): Response<ResponseBody>

    @HTTP(method = "DELETE", path = "api/room/facility/delete", hasBody = true)
    suspend fun deleteFacility(
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

    // Fungsi di bawah ini hanya dibutuhkan jika kamu ingin membuat, edit, atau hapus transaksi booking

    @POST("api/booking/add")
    suspend fun createBooking(
        @Body booking: Booking,
        @Query("access_token") token: String
    ): Response<ResponseBody>

    @PUT("api/booking/update")
    suspend fun updateBooking(
        @Body updatedFields: Map<String, String>,
        @Query("access_token") token: String
    ): Response<ResponseBody>

    @HTTP(method = "DELETE", path = "api/booking/delete", hasBody = true)
    suspend fun deleteBooking(
        @Body payload: Map<String, String>,
        @Query("access_token") token: String
    ): Response<ResponseBody>
}