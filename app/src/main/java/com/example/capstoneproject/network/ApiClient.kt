package com.example.capstoneproject.network

import com.example.capstoneproject.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = Constants.BASE_URL

    init {
        require(BASE_URL.isNotBlank()) { "BASE_URL in Constants must not be blank!" }
    }

    // ✅ Logging Interceptor for full request/response
    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // 🔁 Retry Interceptor (optional, retry up to 3 times)
    private val retryInterceptor = Interceptor { chain ->
        var request = chain.request()
        var response = chain.proceed(request)
        var tryCount = 0
        val maxTries = 3

        while (!response.isSuccessful && tryCount < maxTries) {
            tryCount++
            response.close()
            response = chain.proceed(request)
        }

        response
    }

    // 🔧 Gson config with lenient parsing & nulls support
    private val gson: Gson by lazy {
        GsonBuilder()
            .setLenient()
            .serializeNulls()
            .create()
    }

    // 🌐 OkHttp Client with retry, logging, and custom timeouts
    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(retryInterceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // 🚀 Retrofit instance with configured Gson
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
    }

    // 🔌 API Services
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val adminService: AdminService by lazy {
        retrofit.create(AdminService::class.java)
    }

    val roomService: RoomService by lazy {
        retrofit.create(RoomService::class.java)
    }

    val facilityService: FacilityService by lazy {
        retrofit.create(FacilityService::class.java)
    }

    val roomImageService: RoomImageService by lazy {
        retrofit.create(RoomImageService::class.java)
    }

    val customerService: CustomerService by lazy {
        retrofit.create(CustomerService::class.java)
    }

    val bookingService: BookingService by lazy {
        retrofit.create(BookingService::class.java)
    }
}