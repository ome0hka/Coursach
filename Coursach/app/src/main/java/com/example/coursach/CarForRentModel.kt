package com.example.coursach

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CarForRentModel(
    val id: Int? = null,
    @SerialName("created_at")
    val created_at: String? = null,
    val name: String,
    val pricePerHour: Int,
    val engine: Float,
    val horsePower: Int,
    val icon2: String,
    val icon1: String,
    val icon3: String,
    val rating: Float,
)