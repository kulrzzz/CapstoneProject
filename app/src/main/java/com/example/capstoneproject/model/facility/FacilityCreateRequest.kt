package com.example.capstoneproject.model.facility

data class FacilityCreateRequest(
    val access_token: String,
    val facility_name: String,
    val room_id: String
)