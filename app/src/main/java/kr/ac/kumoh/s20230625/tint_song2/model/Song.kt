package kr.ac.kumoh.s20230625.tint_song2.model

data class Song(
    val id: String,
    val title: String,
    val singer: String,
    val rating: Int,
    val lyrics: String?
)
