package com.example.capstoneproject.model.customer

import com.google.gson.annotations.SerializedName

data class Customer(
    @SerializedName("customer_id") val customer_id: String,
    @SerializedName("customer_fullname") val customer_fullname: String,
    @SerializedName("customer_email") val customer_email: String,
    @SerializedName("customer_pass") val customer_pass: String,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("updated_at") val updated_at: String?,
    @SerializedName("google_id") val google_id: String?
)