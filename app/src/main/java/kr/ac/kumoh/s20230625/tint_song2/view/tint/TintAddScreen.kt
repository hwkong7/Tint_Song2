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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kr.ac.kumoh.s20230625.tint_song2.util.tintColors

@Composable
fun TintAddScreen(
    onDismiss: () -> Unit,
    onAdd: (String, String, String?, String?, Int, String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }

    // 선택된 색
    var selectedColorName by remember { mutableStateOf<String?>(null) }
    var selectedHex by remember { mutableStateOf<String?>(null) }

    // SwiftUI처럼 사용자 커스텀 색(이름+컬러) 저장
    val customColors = remember { mutableStateMapOf<String, Color>() }

    var rating by remember { mutableStateOf(5) }
    var description by remember { mutableStateOf("") }

    // ✅ BottomSheet 크게 + 스크롤 가능 (설명 안 잘리게)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
            // ---------- Form 느낌: Section 스타일 ----------
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

            // ✅ 기본색 + 커스텀색 + '+' 추가까지 포함된 Picker
            TintColorPicker(
                baseColors = tintColors,
                customColors = customColors,
                selectedName = selectedColorName,
                onSelect = { namePicked, hex ->
                    selectedColorName = namePicked
                    selectedHex = hex
                }
            )

            Spacer(Modifier.height(18.dp))
            Text("평점", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))

            // ✅ 1~10 전부 보이게(가로 스크롤) + 글자 안 잘림
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items((1..10).toList()) { score ->
                    FilterChip(
                        selected = rating == score,
                        onClick = { rating = score },
                        label = { Text("${score}점") }
                    )
                }
            }

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
                            rating,
                            description.trim().ifBlank { null }
                        )
                    },
                    enabled = name.isNotBlank() && brand.isNotBlank() && selectedColorName != null,
                    modifier = Modifier.weight(1f)
                ) { Text("추가") }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
