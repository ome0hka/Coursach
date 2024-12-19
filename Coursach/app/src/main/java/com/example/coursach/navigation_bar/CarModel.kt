package com.example.coursach.navigation_bar

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CarModel (
    val id: Int? = null,
    @SerialName("created_at")
    val created_at: String? = null,
    val name: String,
    val price: Int,
    val engine: Float,
    val horsepower: Int,
    val icon: String,
    val vin: String,
    val typeCar: String,
)