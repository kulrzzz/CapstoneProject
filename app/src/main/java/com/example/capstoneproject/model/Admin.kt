package com.example.capstoneproject.model

data class Admin(
    val admin_id: String,
    val admin_fullname: String,
    val admin_email: String,
    val admin_pass: String,
    val admin_who: Int,
    val created_at: String,
    val updated_at: String
)