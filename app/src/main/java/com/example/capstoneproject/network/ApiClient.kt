package com.example.capstoneproject.network

import com.example.capstoneproject.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = "http://3.219.80.4:8000/"

    // =========================================
    // 🪵 Logging Interceptor untuk Debugging
    // =========================================
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // ✅ Gunakan Level.NONE untuk build production
    }

    // =========================================
    // 🌐 OkHttpClient Configuration
    // =========================================
    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Tambahkan logging
        .connectTimeout(10, TimeUnit.SECONDS) // ⏱ Connection timeout
        .readTimeout(15, TimeUnit.SECONDS)    // ⏱ Response timeout
        .writeTimeout(15, TimeUnit.SECONDS)   // ⏱ Optional: waktu upload data
        .build()

    // =========================================
    // 🔧 Retrofit Instance (Gson + OkHttp)
    // =========================================
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Gunakan Gson
            .client(httpClient) // Gunakan client yang sudah diset
            .build()
    }

    // =========================================
    // 🌍 API Service Singleton
    // =========================================
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val adminService: AdminService by lazy {
        retrofit.create(AdminService::class.java)
    }
}