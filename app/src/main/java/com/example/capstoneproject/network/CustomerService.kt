package com.example.capstoneproject.network

import com.example.capstoneproject.model.customer.Customer
import retrofit2.http.GET
import retrofit2.http.Query

interface CustomerService {
    @GET("api/customer/all")
    suspend fun getAllCustomers(
        @Query("access_token") accessToken: String
    ): List<Customer>
}