package com.example.capstoneproject.model

data class RoomWithDetails(
    val room: Room,
    val images: List<RoomImage> = emptyList(),
    val facilities: List<Facility> = emptyList()
)