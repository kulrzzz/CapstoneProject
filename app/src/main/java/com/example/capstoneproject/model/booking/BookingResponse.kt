package com.example.capstoneproject.model.booking

data class BookingResponse(
    val status: String,
    val message: String,
    val data: List<Booking>
)