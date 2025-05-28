package com.example.capstoneproject.network

import com.example.capstoneproject.model.room.*
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

    // ➕ Tambah Ruangan (Room + images + facilities)
    @POST("api/room/add")
    suspend fun createRoom(
        @Body request: RoomWithExtrasCreateRequest
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