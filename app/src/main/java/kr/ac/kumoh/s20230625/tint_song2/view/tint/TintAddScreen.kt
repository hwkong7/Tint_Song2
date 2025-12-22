@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package kr.ac.kumoh.s20230625.tint_song2.view.tint

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import kr.ac.kumoh.s20230625.tint_song2.util.tintColors

@Composable
fun TintAddScreen(
    onDismiss: () -> Unit,
    onAdd: (String, String, String?, String?, Int, String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }

    // 선택된 색(이름/HEX)
    var selectedColorName by remember { mutableStateOf<String?>(null) }
    var selectedHex by remember { mutableStateOf<String?>(null) }

    // SwiftUI처럼 사용자 커스텀 색(이름+컬러) 저장 (칩 리스트에 추가되는 용도)
    val customColors = remember { mutableMapOf<String, Color>() }

    // ✅ 평점: Slider 방식(1~10)
    var rating by remember { mutableStateOf(5f) }
    var description by remember { mutableStateOf("") }

    // ✅ BottomSheet 크게 + 스크롤 가능
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // ✅ iOS 느낌 "직접 색상 선택" 모달
    var showAdvancedPicker by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // ---------- Form 느낌 ----------
            Text("제품 정보 *", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("제품명") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text("브랜드") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(18.dp))
            Text("컬러 선택 *", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))

            // ✅ 기본색 + 커스텀색 + '+' 추가까지 포함된 원형칩 Picker
            TintColorPicker(
                baseColors = tintColors,
                customColors = customColors,
                selectedName = selectedColorName,
                onSelect = { namePicked, hex ->
                    selectedColorName = namePicked
                    selectedHex = hex
                }
            )

            Spacer(Modifier.height(10.dp))

            // ✅ iOS처럼 "직접 색상 선택" 버튼(스펙트럼/격자/슬라이더)
            OutlinedButton(
                onClick = { showAdvancedPicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("직접 색상 선택")
            }

            Spacer(Modifier.height(18.dp))
            Text("평점", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))

            // ✅ SongAddScreen 스타일: Slider + 점수 표시
            Text("${rating.toInt()}점")
            Slider(
                value = rating,
                onValueChange = { rating = it },
                valueRange = 1f..10f,
                steps = 8
            )

            Spacer(Modifier.height(18.dp))
            Text("설명", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("설명") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                singleLine = false
            )

            Spacer(Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) { Text("취소") }

                Button(
                    onClick = {
                        onAdd(
                            name.trim(),
                            brand.trim(),
                            selectedColorName,
                            selectedHex,
                            rating.toInt(),
                            description.trim().ifBlank { null }
                        )
                    },
                    enabled = name.isNotBlank() && brand.isNotBlank() && selectedHex != null,
                    modifier = Modifier.weight(1f)
                ) { Text("추가") }
            }

            Spacer(Modifier.height(24.dp))
        }
    }

    // ✅ "직접 색상 선택" 모달 (AdvancedColorPicker)
    if (showAdvancedPicker) {
        val pickerSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = { showAdvancedPicker = false },
            sheetState = pickerSheetState
        ) {
            // AdvancedColorPicker.kt 파일이 같은 패키지(view.tint)에 있어야 함
            AdvancedColorPicker { selected ->
                // 선택 결과 반영
                selectedColorName = "사용자 지정"
                selectedHex = "#%06X".format(0xFFFFFF and selected.toArgb())

                // 원하면 커스텀 색 목록에도 저장 가능(이름 고정)
                // 같은 이름 중복 방지하려면 timestamp 등 붙여도 됨
                val key = "사용자색"
                customColors[key] = selected

                showAdvancedPicker = false
            }
        }
    }
}
