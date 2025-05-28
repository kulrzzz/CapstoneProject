package com.example.capstoneproject.model.booking

data class CustomerBookingResponse(
    val customer_id: String,
    val customer_fullname: String,
    val customer_email: String,
    val booking: List<BookingDetail>
)