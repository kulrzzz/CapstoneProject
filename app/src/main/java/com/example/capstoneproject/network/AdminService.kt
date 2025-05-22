package com.example.capstoneproject.network

import com.example.capstoneproject.model.Admin
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface AdminService {

    @GET("api/admin/all")
    suspend fun getAllAdmins(
        @Query("access_token") token: String
    ): List<Admin>

    @POST("api/admin/add")
    suspend fun createAdmin(
        @Body admin: Admin,
        @Query("access_token") token: String
    ): Response<ResponseBody>

    @PUT("api/admin/update")
    suspend fun updateAdmin(
        @Body updatedFields: Map<String, String>,
        @Query("access_token") token: String
    ): Response<ResponseBody>

    @HTTP(method = "DELETE", path = "api/admin/delete", hasBody = true)
    suspend fun deleteAdmin(
        @Body payload: Map<String, String>,
        @Query("access_token") token: String
    ): Response<ResponseBody>
}