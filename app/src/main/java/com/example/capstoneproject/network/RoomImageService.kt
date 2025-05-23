package com.example.capstoneproject.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RoomImageService {

    // ================================
    // 📤 POST Upload Gambar Ruangan
    // Auth via Bearer Token (Header)
    // ================================
    @Multipart
    @POST("api/room-image/add")
    suspend fun addRoomImageMultipart(
        @Part image: MultipartBody.Part,
        @Part roomId: MultipartBody.Part,
        @Part accessToken: MultipartBody.Part
    ): Response<ResponseBody>

    // ================================
    // ❌ DELETE Gambar Ruangan
    // Auth via Query Token
    // Payload: {"ri_id": "..."}
    // ================================
    @HTTP(method = "DELETE", path = "api/room-image/delete", hasBody = true)
    suspend fun deleteRoomImage(
        @Body payload: Map<String, String>,
        @Query("access_token") token: String
    ): Response<ResponseBody>
}