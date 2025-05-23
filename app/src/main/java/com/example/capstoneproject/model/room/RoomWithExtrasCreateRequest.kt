package com.example.capstoneproject.model.room

import com.example.capstoneproject.model.room.RoomImageCreatePayload
import com.example.capstoneproject.model.facility.FacilityCreatePayload

data class RoomWithExtrasCreateRequest(
    val access_token: String,
    val room_name: String,
    val room_desc: String,
    val room_kategori: String,
    val room_capacity: Int,
    val room_price: Long,
    val room_available: Int,
    val room_start: String,
    val room_end: String,
    val images: List<RoomImageCreatePayload>,
    val facility: List<FacilityCreatePayload>
)