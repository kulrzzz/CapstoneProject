package com.example.capstoneproject.model.customer

data class Customer(
    val customer_id: String,
    val customer_fullname: String,
    val customer_email: String,
    val customer_pass: String,
    val created_at: String?,
    val updated_at: String?
)