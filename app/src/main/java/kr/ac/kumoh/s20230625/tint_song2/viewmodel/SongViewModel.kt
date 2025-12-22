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

    init { loadSongs() }

    fun loadSongs() {
        viewModelScope.launch {
            try {
                _songs.value = repo.getSongs()
            } catch (e: Exception) {
                Log.e("SongViewModel", "loadSongs: $e")
            }
        }
    }

    fun findSong(id: String): Song? = _songs.value.find { it.id == id }

    fun addSong(title: String, singer: String, rating: Int, lyrics: String?) {
        val newSong = Song(
            id = UUID.randomUUID().toString(),
            title = title,
            singer = singer,
            rating = rating,
            lyrics = lyrics
        )
        viewModelScope.launch {
            try {
                repo.addSong(newSong)
                _songs.value = _songs.value + newSong
            } catch (e: Exception) {
                Log.e("SongViewModel", "addSong: $e")
            }
        }
    }

    fun deleteSong(id: String) {
        viewModelScope.launch {
            try {
                repo.deleteSong(id)
                _songs.value = _songs.value.filter { it.id != id }
            } catch (e: Exception) {
                Log.e("SongViewModel", "deleteSong: $e")
            }
        }
    }
}
