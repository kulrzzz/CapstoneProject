package com.example.capstoneproject.model.admin

data class AdminUpdateRequest(
    val access_token: String,
    val admin_id: String,
    val admin_fullname: String?,
    val admin_email: String?,
    val admin_pass: String?,
    val admin_who: Int?
)