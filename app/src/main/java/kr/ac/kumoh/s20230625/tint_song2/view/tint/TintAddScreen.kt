@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package kr.ac.kumoh.s20230625.tint_song2.view.tint

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.ac.kumoh.s20230625.tint_song2.util.tintColors
import kr.ac.kumoh.s20230625.tint_song2.util.ColorHex

@Composable
fun TintAddScreen(
    onDismiss: () -> Unit,
    onAdd: (String, String, String?, String?, Int, String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf<String?>(null) }
    var selectedHex by remember { mutableStateOf<String?>(null) }
    var rating by remember { mutableStateOf(5) }
    var description by remember { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp)) {
            Text("제품 정보 *")
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("제품명") })
            OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("브랜드") })

            Spacer(Modifier.height(12.dp))
            Text("컬러 선택")
            TintColorPicker(
                tintColors = tintColors,
                selected = selectedColor,
                onSelect = { colorName, color ->
                    selectedColor = colorName
                    selectedHex = ColorHex.toHex(color)
                }
            )

            Spacer(Modifier.height(12.dp))
            Text("평점")
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                (1..10).forEach { score ->
                    FilterChip(
                        selected = rating == score,
                        onClick = { rating = score },
                        label = { Text("${score}점") }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("설명") },
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )

            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("취소") }
                Button(
                    onClick = {
                        onAdd(name, brand, selectedColor, selectedHex, rating, description)
                    },
                    enabled = name.isNotBlank() && brand.isNotBlank() && selectedColor != null,
                    modifier = Modifier.weight(1f)
                ) { Text("추가") }
            }
        }
    }
}
