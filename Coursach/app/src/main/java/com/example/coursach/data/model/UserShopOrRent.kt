package com.example.coursach.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserShopOrRent(
    val uuid: String? = null,
    @SerialName("created_at")
    val created_at: String? = null,
    val isWantShop: Boolean,
    val isWantRent: Boolean,
)
