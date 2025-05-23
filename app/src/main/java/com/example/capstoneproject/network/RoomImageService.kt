package com.example.capstoneproject.network

import com.example.capstoneproject.model.room.RoomImageDeleteRequest
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RoomImageService {

    // 📤 Upload Gambar Ruangan (via Multipart)
    @Multipart
    @POST("api/room-image/add")
    suspend fun addRoomImageMultipart(
        @Part image: MultipartBody.Part,
        @Part("room_id") roomId: MultipartBody.Part,
        @Part("access_token") accessToken: MultipartBody.Part
    ): Response<ResponseBody>

    // ❌ Hapus Gambar Ruangan
    @HTTP(method = "DELETE", path = "api/room-image/delete", hasBody = true)
    suspend fun deleteRoomImage(
        @Body request: RoomImageDeleteRequest
    ): Response<ResponseBody>
}