package com.example.capstoneproject.model

data class BookingResponse(
    val status: String,
    val message: String,
    val data: List<Booking>
)