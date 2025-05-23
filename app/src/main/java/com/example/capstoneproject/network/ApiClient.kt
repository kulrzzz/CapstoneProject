package com.example.capstoneproject.network

import com.example.capstoneproject.util.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// ==========================
// 🔐 Interceptor untuk Auth
// ==========================
class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestWithAuth = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(requestWithAuth)
    }
}

object ApiClient {

    private const val BASE_URL = Constants.BASE_URL

    // ==========================
    // 🪵 Logging Interceptor
    // ==========================
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // ==========================
    // 🌐 OkHttpClient Tanpa Auth
    // ==========================
    private val httpClientNoAuth: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    // ==========================
    // 🔧 Retrofit Tanpa Auth
    // ==========================
    private val retrofitNoAuth: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClientNoAuth)
            .build()
    }

    // ==========================
    // 🌍 API Service Tanpa Auth
    // ==========================
    val apiService: ApiService by lazy {
        retrofitNoAuth.create(ApiService::class.java)
    }

    val adminService: AdminService by lazy {
        retrofitNoAuth.create(AdminService::class.java)
    }

    // ======================================================
    // 🌐 Retrofit Instance Dengan Authorization Header
    // ======================================================
    fun getClientWithAuth(token: String): Retrofit {
        val clientWithAuth = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(token))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientWithAuth)
            .build()
    }
}