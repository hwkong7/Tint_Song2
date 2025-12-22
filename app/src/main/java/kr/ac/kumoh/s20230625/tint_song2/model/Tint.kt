package kr.ac.kumoh.s20230625.tint_song2.model

import com.google.gson.annotations.SerializedName

data class Tint(
    val id: String,
    @SerializedName("product_name")
    val productName: String,
    val brand: String,
    @SerializedName("color_family")
    val colorFamily: String?,
    @SerializedName("color_hex")
    val colorHex: String?,
    val rating: Int,
    val description: String?
)
