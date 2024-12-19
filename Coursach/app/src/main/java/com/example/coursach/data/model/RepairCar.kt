package com.example.coursach.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepairCar(
    val id: Int? = null,
    @SerialName("created_at")
    val created_at: String? = null,
    val uuid: String,
    val typeRepair: String,
    val vin: String,
)

