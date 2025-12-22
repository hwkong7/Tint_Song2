package kr.ac.kumoh.s20230625.tint_song2.repository

import kr.ac.kumoh.s20230625.tint_song2.api.SupabaseApiConfig
import kr.ac.kumoh.s20230625.tint_song2.model.Tint

class TintRepository(
    private val api: kr.ac.kumoh.s20230625.tint_song2.api.SupabaseService = SupabaseApiConfig.service
) {
    suspend fun fetchTints(): List<Tint> = api.getTints()

    suspend fun addTint(tint: Tint) {
        val res = api.addTint(tint)
        if (!res.isSuccessful) {
            throw RuntimeException("addTint 실패: ${res.code()} ${res.errorBody()?.string()}")
        }
    }

    suspend fun deleteTint(id: String) {
        val res = api.deleteTint("eq.$id")
        if (!res.isSuccessful) {
            throw RuntimeException("deleteTint 실패: ${res.code()} ${res.errorBody()?.string()}")
        }
    }
}
