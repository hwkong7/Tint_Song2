package kr.ac.kumoh.s20230625.tint_song2.repository

import kr.ac.kumoh.s20230625.tint_song2.api.SupabaseApiConfig
import kr.ac.kumoh.s20230625.tint_song2.model.Song

class SongRepository {
    private val api = SupabaseApiConfig.service

    suspend fun getSongs(): List<Song> = api.getSongs()

    suspend fun addSong(song: Song) {
        api.addSong(song)
    }

    suspend fun deleteSong(id: String) {
        api.deleteSong(filter = "eq.$id")
    }
}