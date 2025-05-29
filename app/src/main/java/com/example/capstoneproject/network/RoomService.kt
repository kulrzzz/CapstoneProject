package com.example.capstoneproject.network

import com.example.capstoneproject.model.room.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RoomService {

    // 📥 GET Semua Ruangan
    @GET("api/room/all")
    suspend fun getAllRooms(
        @Query("access_token") token: String
    ): List<Room>

    // 📄 GET Detail Ruangan (termasuk image + facility)
    @GET("api/room/detail/{room_id}")
    suspend fun getRoomDetail(
        @Path("room_id") roomId: String,
        @Query("access_token") token: String
    ): RoomWithDetails

    // ✅ ➕ Tambah Ruangan (dengan fasilitas dan gambar) - multipart
    @Multipart
    @POST("api/room/full-add")
    suspend fun createRoomMultipart(
        @Part("access_token") accessToken: RequestBody,
        @Part("room_name") roomName: RequestBody,
        @Part("room_desc") roomDesc: RequestBody,
        @Part("room_kategori") roomKategori: RequestBody,
        @Part("room_capacity") roomCapacity: RequestBody,
        @Part("room_price") roomPrice: RequestBody,
        @Part("room_available") roomAvailable: RequestBody,
        @Part("room_start") roomStart: RequestBody,
        @Part("room_end") roomEnd: RequestBody,
        @Part("facility") facilityJson: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): RoomSingleResponse

    // ✏️ Update Ruangan
    @PUT("api/room/update")
    suspend fun updateRoom(
        @Body request: RoomUpdateRequest
    ): Response<ResponseBody>

    // ❌ Hapus Ruangan
    @HTTP(method = "DELETE", path = "api/room/delete", hasBody = true)
    suspend fun deleteRoom(
        @Body request: RoomDeleteRequest
    ): Response<ResponseBody>
}