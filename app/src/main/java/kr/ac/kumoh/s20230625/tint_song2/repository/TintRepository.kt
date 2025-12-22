package kr.ac.kumoh.s20230625.tint_song2.repository

import kr.ac.kumoh.s20230625.tint_song2.api.SupabaseApiConfig
import kr.ac.kumoh.s20230625.tint_song2.model.Tint

class TintRepository {
    private val api = SupabaseApiConfig.service

    suspend fun getTints(): List<Tint> = api.getTints()

    suspend fun addTint(tint: Tint) {
        api.addTint(tint)
    }

    suspend fun deleteTint(id: String) {
        api.deleteTint(filter = "eq.$id")
    }
}