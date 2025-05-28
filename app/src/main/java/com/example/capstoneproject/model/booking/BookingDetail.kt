package com.example.capstoneproject.model.booking

data class BookingDetail(
    val booking_id: String,
    val booking_code: String,
    val room_name: String,
    val booking_date: String,
    val booking_start: String,
    val booking_end: String,
    val booking_desc: String,
    val booking_price: Long
)