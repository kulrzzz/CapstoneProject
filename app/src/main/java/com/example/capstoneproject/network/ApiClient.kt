package com.example.capstoneproject.network

import com.example.capstoneproject.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = Constants.BASE_URL

    init {
        require(BASE_URL.isNotBlank()) { "BASE_URL in Constants must not be blank!" }
    }

    // 🪵 Logging interceptor: log full body of request & response
    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // 🌐 OkHttp client with timeout and logging
    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    // 🔧 Retrofit builder with GSON
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    // 🔌 Services (reusable instances)
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