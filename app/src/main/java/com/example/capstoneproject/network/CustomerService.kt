package com.example.capstoneproject.network

import com.example.capstoneproject.model.customer.Customer
import com.example.capstoneproject.model.customer.CustomerDeleteRequest
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HTTP

interface CustomerService {
    @GET("api/customer/all")
    suspend fun getAllCustomers(
        @Query("access_token") accessToken: String
    ): List<Customer>

    @HTTP(method = "DELETE", path = "api/customer/delete", hasBody = true)
    suspend fun deleteCustomer(
        @Body body: CustomerDeleteRequest
    ): Response<Void>
}