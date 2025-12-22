package kr.ac.kumoh.s20230625.tint_song2.api

import kr.ac.kumoh.s20230625.tint_song2.model.Song
import kr.ac.kumoh.s20230625.tint_song2.model.Tint
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseService {
    // ---- Songs ----
    @GET("songs")
    suspend fun getSongs(
        @Query("select") select: String = "*"
    ): List<Song>

    @POST("songs")
    suspend fun addSong(
        @Body song: Song,
        @Header("Prefer") prefer: String = "return=minimal"
    ): Response<Unit>

    @DELETE("songs")
    suspend fun deleteSong(
        @Query("id") filter: String,
        @Header("Prefer") prefer: String = "return=minimal"
    ): Response<Unit>

    // ---- Tints (cosmetics) ----
    @GET("cosmetics")
    suspend fun getTints(
        @Query("select") select: String = "*"
    ): List<Tint>

    @POST("cosmetics")
    suspend fun addTint(
        @Body tint: Tint,
        @Header("Prefer") prefer: String = "return=minimal"
    ): Response<Unit>

    @DELETE("cosmetics")
    suspend fun deleteTint(
        @Query("id") filter: String,
        @Header("Prefer") prefer: String = "return=minimal"
    ): Response<Unit>
}
