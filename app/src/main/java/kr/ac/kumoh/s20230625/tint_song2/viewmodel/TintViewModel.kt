package kr.ac.kumoh.s20230625.tint_song2.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ac.kumoh.s20230625.tint_song2.model.Tint
import kr.ac.kumoh.s20230625.tint_song2.repository.TintRepository
import java.util.UUID

class TintViewModel(
    private val repo: TintRepository = TintRepository()
) : ViewModel() {

    private val _tints = MutableStateFlow<List<Tint>>(emptyList())
    val tints = _tints.asStateFlow()

    fun findTint(id: String): Tint? {
        return tints.value.firstOrNull { it.id == id }
    }

    fun loadTints() {
        viewModelScope.launch {
            try {
                _tints.value = repo.fetchTints()
            } catch (e: Exception) {
                Log.e("TintVM", "loadTints 실패: ${e.message}", e)
            }
        }
    }

    fun addTint(
        productName: String,
        brand: String,
        colorFamily: String?,
        colorHex: String?,
        rating: Int,
        description: String?
    ) {
        viewModelScope.launch {
            try {
                val tint = Tint(
                    id = UUID.randomUUID().toString(), // ✅ 문자열 UUID
                    productName = productName,
                    brand = brand,
                    colorFamily = colorFamily,
                    colorHex = colorHex,
                    rating = rating,
                    description = description
                )
                repo.addTint(tint)
                loadTints() // ✅ 추가 후 즉시 갱신
            } catch (e: Exception) {
                Log.e("TintVM", "addTint 실패: ${e.message}", e)
            }
        }
    }

    fun deleteTint(id: String) {
        viewModelScope.launch {
            try {
                repo.deleteTint(id)
                loadTints() // ✅ 삭제 후 즉시 갱신
            } catch (e: Exception) {
                Log.e("TintVM", "deleteTint 실패: ${e.message}", e)
            }
        }
    }
}
