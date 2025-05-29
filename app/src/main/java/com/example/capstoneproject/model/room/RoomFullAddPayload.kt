package com.example.capstoneproject.model.room

import com.example.capstoneproject.model.facility.FacilityCreatePayload
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class FullAddRoomPayload(
    val token: String,
    val room_name: String,
    val room_desc: String,
    val room_kategori: String,
    val room_capacity: Int,
    val room_price: Long,
    val room_available: Int,
    val room_start: String,
    val room_end: String,
    val facilities: List<FacilityCreatePayload>,
    val imageFiles: List<File>
) {
    fun toMultipartParts(): Pair<Map<String, RequestBody>, List<MultipartBody.Part>> {
        val textParts = mutableMapOf<String, RequestBody>()

        textParts["access_token"] = token.toRequestBody("text/plain".toMediaTypeOrNull())
        textParts["room_name"] = room_name.toRequestBody("text/plain".toMediaTypeOrNull())
        textParts["room_desc"] = room_desc.toRequestBody("text/plain".toMediaTypeOrNull())
        textParts["room_kategori"] = room_kategori.toRequestBody("text/plain".toMediaTypeOrNull())
        textParts["room_capacity"] = room_capacity.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        textParts["room_price"] = room_price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        textParts["room_available"] = room_available.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        textParts["room_start"] = room_start.toRequestBody("text/plain".toMediaTypeOrNull())
        textParts["room_end"] = room_end.toRequestBody("text/plain".toMediaTypeOrNull())

        val facilityJson = Gson().toJson(facilities)
        textParts["facility"] = facilityJson.toRequestBody("text/plain".toMediaTypeOrNull())

        val imageParts = imageFiles.map { file ->
            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images[]", file.name, reqFile)
        }

        return Pair(textParts, imageParts)
    }
}