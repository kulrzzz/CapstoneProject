package com.example.capstoneproject.model

data class Admin(
    val admin_id: String? = null,
    val admin_fullname: String,
    val admin_email: String,
    val admin_pass: String,
    val admin_who: Int? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)