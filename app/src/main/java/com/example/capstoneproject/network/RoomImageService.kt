package com.example.capstoneproject.network

import com.example.capstoneproject.model.room.RoomImageDeleteRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RoomImageService {

    @Multipart
    @POST("api/room-image/add")
    suspend fun addRoomImageMultipart(
        @Part("ri_image") image: MultipartBody.Part,
        @Part("room_id") roomId: RequestBody,
        @Part("access_token") accessToken: RequestBody
    ): Response<ResponseBody>

    @HTTP(method = "DELETE", path = "api/room-image/delete", hasBody = true)
    suspend fun deleteRoomImage(
        @Body request: RoomImageDeleteRequest
    ): Response<ResponseBody>
}