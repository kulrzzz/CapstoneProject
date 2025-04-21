package com.example.capstoneproject.model

data class CustomerResponse(
    val success: Boolean,
    val data: List<CustomerData>
)

data class CustomerData(
    val customer_id: String,
    val customer_fullname: String,
    val customer_email: String,
    val created_at: String,
    val updated_at: String
)