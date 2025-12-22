package kr.ac.kumoh.s20230625.tint_song2.api

import kr.ac.kumoh.s20230625.tint_song2.model.Song
import kr.ac.kumoh.s20230625.tint_song2.model.Tint
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseService {
    // ---- Songs ----
    @GET("songs")
    suspend fun getSongs(
        @Query("apikey") apikey: String = SupabaseApiConfig.API_KEY
    ): List<Song>

    @POST("songs")
    suspend fun addSong(
        @Body song: Song,
        @Query("apikey") apikey: String = SupabaseApiConfig.API_KEY
    ): Unit

    @DELETE("songs")
    suspend fun deleteSong(
        @Query("id") filter: String,
        @Query("apikey") apikey: String = SupabaseApiConfig.API_KEY
    ): Unit

    // ---- Tints (cosmetics) ----
    @GET("cosmetics")
    suspend fun getTints(
        @Query("apikey") apikey: String = SupabaseApiConfig.API_KEY
    ): List<Tint>

    @POST("cosmetics")
    suspend fun addTint(
        @Body tint: Tint,
        @Query("apikey") apikey: String = SupabaseApiConfig.API_KEY
    ): Unit

    @DELETE("cosmetics")
    suspend fun deleteTint(
        @Query("id") filter: String,
        @Query("apikey") apikey: String = SupabaseApiConfig.API_KEY
    ): Unit
}