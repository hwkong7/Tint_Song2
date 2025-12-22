package kr.ac.kumoh.s20230625.tint_song2.repository

import kr.ac.kumoh.s20230625.tint_song2.api.SupabaseApiConfig
import kr.ac.kumoh.s20230625.tint_song2.model.Song

class SongRepository(
    private val api: kr.ac.kumoh.s20230625.tint_song2.api.SupabaseService = SupabaseApiConfig.service
) {
    suspend fun fetchSongs(): List<Song> = api.getSongs()

    suspend fun addSong(song: Song) {
        val res = api.addSong(song)
        if (!res.isSuccessful) {
            throw RuntimeException("addSong 실패: ${res.code()} ${res.errorBody()?.string()}")
        }
    }

    suspend fun deleteSong(id: String) {
        val res = api.deleteSong("eq.$id")
        if (!res.isSuccessful) {
            throw RuntimeException("deleteSong 실패: ${res.code()} ${res.errorBody()?.string()}")
        }
    }
}
