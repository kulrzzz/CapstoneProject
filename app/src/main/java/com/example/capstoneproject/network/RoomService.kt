package com.example.capstoneproject.network

import com.example.capstoneproject.model.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RoomService {

    // ================================
    // 📥 GET All Rooms (List Response)
    // ================================
    @GET("api/room/all")
    suspend fun getAllRooms(
        @Query("access_token") token: String
    ): RoomResponse  // RoomResponse: status + data: List<Room>

    // ================================
    // ➕ POST Add Room (x-www-form-urlencoded)
    // ================================
    @FormUrlEncoded
    @POST("api/room/add")
    suspend fun addRoomForm(
        @FieldMap roomFields: Map<String, String>,
        @Query("access_token") token: String
    ): Response<RoomSingleResponse>

    // ================================
    // ✏️ PUT Update Room
    // ================================
    @FormUrlEncoded
    @PUT("api/room/update")
    suspend fun updateRoom(
        @FieldMap updatedFields: Map<String, String>,
        @Query("access_token") token: String
    ): Response<ResponseBody>

    // ================================
    // ❌ DELETE Room
    // ================================
    @HTTP(method = "DELETE", path = "api/room/delete", hasBody = true)
    suspend fun deleteRoom(
        @Body payload: Map<String, String>,
        @Query("access_token") token: String
    ): Response<ResponseBody>

    // ================================
    // 📄 GET Room Detail + Images + Facilities
    // ================================
    @GET("api/room/detail/{room_id}")
    suspend fun getRoomDetail(
        @Path("room_id") roomId: String,
        @Query("access_token") token: String
    ): RoomWithDetails
}