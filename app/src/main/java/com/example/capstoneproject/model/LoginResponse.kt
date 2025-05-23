package com.example.capstoneproject.model

import com.example.capstoneproject.model.admin.Admin

data class LoginResponse(
    val status: String,
    val message: String?,
    val role: String?,
    val admin: Admin
)