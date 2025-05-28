package com.example.capstoneproject.model.customer

data class CustomerDeleteRequest(
    val access_token: String,
    val customer_id: String
)