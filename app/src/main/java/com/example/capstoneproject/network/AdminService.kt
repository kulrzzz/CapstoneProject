package com.example.capstoneproject.network

import com.example.capstoneproject.model.admin.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface AdminService {

    // 📋 GET Semua Data Admin
    @GET("api/admin/all")
    suspend fun getAllAdmins(
        @Query("access_token") token: String
    ): List<Admin>

    // 📄 GET Detail Admin (mengembalikan objek Admin penuh)
    @GET("api/admin/detail/{admin_id}")
    suspend fun getAdminDetail(
        @Path("admin_id") adminId: String,
        @Query("access_token") token: String
    ): AdminResponse

    // ➕ Tambah Admin
    @POST("api/admin/add")
    suspend fun createAdmin(
        @Body request: AdminCreateRequest
    ): Response<AdminCreateResponse> // gunakan model response khusus create

    // ✏️ Update Admin (karena responsenya tidak stabil, gunakan ResponseBody)
    @PUT("api/admin/update")
    suspend fun updateAdmin(
        @Body request: AdminUpdateRequest
    ): Response<ResponseBody>

    // ❌ Hapus Admin
    @HTTP(method = "DELETE", path = "api/admin/delete", hasBody = true)
    suspend fun deleteAdmin(
        @Body request: AdminDeleteRequest
    ): Response<ResponseBody>
}