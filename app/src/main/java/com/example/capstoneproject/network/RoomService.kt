package com.example.capstoneproject.network

import com.example.capstoneproject.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RoomService {

    // =======================================
    // 📦 Ambil Semua Data Ruangan
    // =======================================
    @GET("api/room/all")
    suspend fun getAllRooms(
        @Query("access_token") token: String
    ): List<Room>

    // =======================================
    // ➕ Tambah Ruangan Baru
    // =======================================
    @POST("api/room/add")
    suspend fun createRoom(
        @Body room: RoomAddRequest,
        @Query("access_token") token: String
    ): Response<ResponseBody>

    // =======================================
    // ➕ Tambah Fasilitas ke Ruangan
    // =======================================
    @FormUrlEncoded
    @POST("api/room-facility/add")
    suspend fun addFacilityToRoom(
        @Field("facility_name") facilityName: String,
        @Field("room_id") roomId: String,
        @Query("access_token") token: String
    ): Response<ResponseBody>

    // =======================================
    // 🖼 Upload Gambar ke Ruangan
    // =======================================
    @Multipart
    @POST("api/room-image/add")
    suspend fun uploadRoomImage(
        @Part ri_image: MultipartBody.Part,
        @Part("room_id") roomId: RequestBody,
        @Query("access_token") token: String
    ): Response<ResponseBody>
}