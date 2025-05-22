package com.example.capstoneproject.model

data class LoginResponse(
    val status: String,
    val message: String,
    val rule: String,
    val admin: Admin
)