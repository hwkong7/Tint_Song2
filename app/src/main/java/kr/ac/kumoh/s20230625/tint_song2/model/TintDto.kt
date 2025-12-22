package kr.ac.kumoh.s20230625.tint_song2.model

import com.google.gson.annotations.SerializedName

data class TintDto(
    @SerializedName("id") val id: String? = null,

    @SerializedName("product_name") val productName: String,
    @SerializedName("brand") val brand: String,

    @SerializedName("color_family") val colorFamily: String? = null,
    @SerializedName("color_hex") val colorHex: String? = null,

    @SerializedName("rating") val rating: Int,
    @SerializedName("description") val description: String? = null
)
