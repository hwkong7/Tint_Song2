package kr.ac.kumoh.s20230625.tint_song2.view.tint

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kr.ac.kumoh.s20230625.tint_song2.util.ColorHex

@Composable
fun TintColorPicker(
    baseColors: Map<String, Color>,
    customColors: MutableMap<String, Color>,
    selectedName: String?,
    onSelect: (name: String, hex: String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }

    // 간단 RGB 슬라이더 방식(외부 라이브러리 없이 구현)
    var r by remember { mutableFloatStateOf(255f) }
    var g by remember { mutableFloatStateOf(105f) }
    var b by remember { mutableFloatStateOf(180f) }

    val keys = remember(baseColors.size, customColors.size) {
        (baseColors.keys + customColors.keys).toList().sorted()
    }

    fun colorOf(name: String): Color = customColors[name] ?: baseColors[name] ?: Color.LightGray

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(keys) { key ->
            val color = colorOf(key)
            val selected = selectedName == key

            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable { onSelect(key, ColorHex.toHex(color)) }
                )

                if (selected) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Black, CircleShape)
                    )
                }
            }
        }

        item {
            IconButton(onClick = { showDialog = true }) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFEFEF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = "색상 추가")
                }
            }
        }
    }

    if (showDialog) {
        val preview = Color(r / 255f, g / 255f, b / 255f, 1f)

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("새 색상 추가") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(preview)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(ColorHex.toHex(preview))
                    }

                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("색상 이름 (예: 로지핑크)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("R: ${r.toInt()}"); Slider(r, { r = it }, valueRange = 0f..255f)
                    Text("G: ${g.toInt()}"); Slider(g, { g = it }, valueRange = 0f..255f)
                    Text("B: ${b.toInt()}"); Slider(b, { b = it }, valueRange = 0f..255f)
                }
            },
            confirmButton = {
                TextButton(
                    enabled = newName.isNotBlank(),
                    onClick = {
                        val key = newName.trim()
                        customColors[key] = preview
                        onSelect(key, ColorHex.toHex(preview))
                        newName = ""
                        showDialog = false
                    }
                ) { Text("추가") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("취소") }
            }
        )
    }
}
