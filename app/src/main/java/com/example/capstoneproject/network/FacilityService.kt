package com.example.capstoneproject.network

import com.example.capstoneproject.model.FacilityResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface FacilityService {

    // ================================
    // ➕ Tambah Fasilitas ke Ruangan
    // Response: status + data: Facility
    // ================================
    @FormUrlEncoded
    @POST("api/room-facility/add")
    suspend fun addFacility(
        @Field("facility_name") facilityName: String,
        @Field("room_id") roomId: String,
        @Query("access_token") token: String
    ): Response<FacilityResponse>
    // 🔁 Ganti ke: Response<ResponseBody> jika backend masih kirim string JSON

    // ================================
    // ❌ Hapus Fasilitas dari Ruangan
    // Payload: {"facility_id": "..."}
    // ================================
    @HTTP(method = "DELETE", path = "api/room-facility/delete", hasBody = true)
    suspend fun deleteFacility(
        @Body payload: Map<String, String>,
        @Query("access_token") token: String
    ): Response<ResponseBody>
}