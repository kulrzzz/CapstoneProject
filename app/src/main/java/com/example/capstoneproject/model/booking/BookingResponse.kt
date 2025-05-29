package com.example.capstoneproject.model.booking

data class BookingResponse(
    val status: Boolean,
    val message: String,
    val data: List<BookingRawItem>
)