package com.example.capstoneproject.network

import com.example.capstoneproject.model.facility.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface FacilityService {

    // ➕ Tambah Fasilitas ke Ruangan
    @POST("api/room-facility/add")
    suspend fun addFacility(
        @Body request: FacilityCreateRequest
    ): Response<FacilityResponse>

    // ❌ Hapus Fasilitas dari Ruangan
    @HTTP(method = "DELETE", path = "api/room-facility/delete", hasBody = true)
    suspend fun deleteFacility(
        @Body request: FacilityDeleteRequest
    ): Response<ResponseBody>
}