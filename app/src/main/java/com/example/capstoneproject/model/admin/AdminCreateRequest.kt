package com.example.capstoneproject.model.admin

data class AdminCreateRequest(
    val access_token: String,
    val admin_fullname: String,
    val admin_email: String,
    val admin_pass: String,
    val admin_who: Int
)