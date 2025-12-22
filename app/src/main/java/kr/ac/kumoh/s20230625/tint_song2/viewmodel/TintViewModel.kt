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

    init { loadTints() }

    fun loadTints() {
        viewModelScope.launch {
            try {
                _tints.value = repo.getTints()
            } catch (e: Exception) {
                Log.e("TintViewModel", "loadTints: $e")
            }
        }
    }

    fun findTint(id: String): Tint? = _tints.value.find { it.id == id }

    fun addTint(
        productName: String,
        brand: String,
        colorFamily: String?,
        colorHex: String?,
        rating: Int,
        description: String?
    ) {
        val newTint = Tint(
            id = UUID.randomUUID().toString(),
            productName = productName,
            brand = brand,
            colorFamily = colorFamily,
            colorHex = colorHex,
            rating = rating,
            description = description
        )
        viewModelScope.launch {
            try {
                repo.addTint(newTint)
                _tints.value = _tints.value + newTint
            } catch (e: Exception) {
                Log.e("TintViewModel", "addTint: $e")
            }
        }
    }

    fun deleteTint(id: String) {
        viewModelScope.launch {
            try {
                repo.deleteTint(id)
                _tints.value = _tints.value.filter { it.id != id }
            } catch (e: Exception) {
                Log.e("TintViewModel", "deleteTint: $e")
            }
        }
    }
}
