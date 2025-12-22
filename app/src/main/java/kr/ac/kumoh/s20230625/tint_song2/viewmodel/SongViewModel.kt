package kr.ac.kumoh.s20230625.tint_song2.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ac.kumoh.s20230625.tint_song2.model.Song
import kr.ac.kumoh.s20230625.tint_song2.repository.SongRepository
import java.util.UUID

class SongViewModel(
    private val repo: SongRepository = SongRepository()
) : ViewModel() {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs = _songs.asStateFlow()
    fun findSong(id: String): Song? {
        return songs.value.firstOrNull { it.id == id }
    }
    fun loadSongs() {
        viewModelScope.launch {
            try {
                _songs.value = repo.fetchSongs()
            } catch (e: Exception) {
                Log.e("SongVM", "loadSongs 실패: ${e.message}", e)
            }
        }
    }

    fun addSong(title: String, singer: String, rating: Int, lyrics: String?) {
        viewModelScope.launch {
            try {
                val song = Song(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    singer = singer,
                    rating = rating,
                    lyrics = lyrics
                )
                repo.addSong(song)
                loadSongs()
            } catch (e: Exception) {
                Log.e("SongVM", "addSong 실패: ${e.message}", e)
            }
        }
    }

    fun deleteSong(id: String) {
        viewModelScope.launch {
            try {
                repo.deleteSong(id)
                loadSongs()
            } catch (e: Exception) {
                Log.e("SongVM", "deleteSong 실패: ${e.message}", e)
            }
        }
    }
}
