package com.example.capstoneproject.model.room

import com.example.capstoneproject.model.facility.Facility

data class RoomWithDetails(
    val room_id: String,
    val room_name: String,
    val room_desc: String,
    val room_kategori: String,
    val room_capacity: Int,
    val room_price: Long,
    val room_available: Int,
    val room_start: String,
    val room_end: String,
    val created_at: String,
    val updated_at: String,
    val images: List<RoomImage>,
    val facility: List<Facility>
)