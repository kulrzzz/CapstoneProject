package com.example.capstoneproject.network

import com.example.capstoneproject.util.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.ConcurrentHashMap

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

    init {
        require(BASE_URL.isNotEmpty()) { "BASE_URL harus di-set di Constants" }
    }

    // ==========================
    // 🪵 Logging Interceptor
    // ==========================
    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // ==========================
    // 🌐 OkHttpClient Tanpa Auth
    // ==========================
    private val httpClientNoAuth: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

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
    // 🌍 API Services Tanpa Auth
    // ==========================
    val apiService: ApiService by lazy {
        retrofitNoAuth.create(ApiService::class.java)
    }

    val adminService: AdminService by lazy {
        retrofitNoAuth.create(AdminService::class.java)
    }

    // ============================================
    // 🔄 Retrofit Instance Dengan Auth + Caching
    // ============================================
    private val retrofitCache = ConcurrentHashMap<String, Retrofit>()

    fun getClientWithAuth(token: String): Retrofit {
        return retrofitCache[token] ?: createRetrofitWithAuth(token).also {
            retrofitCache[token] = it
        }
    }

    private fun createRetrofitWithAuth(token: String): Retrofit {
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