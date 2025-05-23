package com.example.capstoneproject.model.room

data class RoomUpdateRequest(
    val access_token: String,
    val room_id: String,
    val room_name: String?,
    val room_desc: String?,
    val room_kategori: String?,
    val room_capacity: Int?,
    val room_price: Long?,
    val room_available: Int?,
    val room_start: String?,
    val room_end: String?
)