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
import androidx.compose.ui.unit.dp
import kr.ac.kumoh.s20230625.tint_song2.util.tintColors

@Composable
fun TintAddScreen(
    onDismiss: () -> Unit,
    onAdd: (String, String, String?, String?, Int, String?) -> Unit
) {
    // ---------------- 기본 입력 상태 ----------------
    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var selectedColorName by remember { mutableStateOf<String?>(null) }
    var selectedHex by remember { mutableStateOf<String?>(null) }
    val customColors = remember { mutableMapOf<String, Color>() }
    var rating by remember { mutableStateOf(5f) }
    var description by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // ---------------- BottomSheet ----------------
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
            // ===== 제목 =====
            Text(
                "제품 정보 *",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Black
            )
            Spacer(Modifier.height(8.dp))

            // ===== 제품명 / 브랜드 =====
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

            // ===== 색상 선택 =====
            Spacer(Modifier.height(18.dp))
            Text(
                "컬러 선택 *",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Black
            )
            Spacer(Modifier.height(8.dp))

            TintColorPicker(
                baseColors = tintColors,
                customColors = customColors,
                selectedName = selectedColorName,
                onSelect = { namePicked, hex ->
                    selectedColorName = namePicked
                    selectedHex = hex
                }
            )

            // ===== 평점 =====
            Spacer(Modifier.height(18.dp))
            Text(
                "평점",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Black
            )
            Spacer(Modifier.height(8.dp))

            Text("${rating.toInt()}점")
            Slider(
                value = rating,
                onValueChange = { rating = it },
                valueRange = 1f..10f,
                steps = 8
            )

            // ===== 설명 =====
            Spacer(Modifier.height(18.dp))
            Text(
                "설명",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Black
            )
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

            // ===== 버튼 =====
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
                    enabled = name.isNotBlank() && brand.isNotBlank() && selectedColorName != null,
                    modifier = Modifier.weight(1f)
                ) { Text("추가") }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
