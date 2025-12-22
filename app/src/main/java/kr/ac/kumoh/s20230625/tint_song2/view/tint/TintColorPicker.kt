@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
    var showSheet by remember { mutableStateOf(false) }

    // 시트 내부 상태
    var newName by remember { mutableStateOf("") }
    var r by remember { mutableFloatStateOf(255f) }
    var g by remember { mutableFloatStateOf(105f) }
    var b by remember { mutableFloatStateOf(180f) }
    var a by remember { mutableFloatStateOf(1f) } // 0~1

    fun colorOf(name: String): Color =
        customColors[name] ?: baseColors[name] ?: Color.LightGray

    // 바깥 팔레트 키 목록 (base + custom)
    val keys: List<String> =
        (baseColors.keys + customColors.keys).distinct().sorted()

    // 현재 슬라이더 색상 (미리보기)
    val preview = Color(r / 255f, g / 255f, b / 255f, a)
    val hex = ColorHex.toHex(Color(r / 255f, g / 255f, b / 255f, 1f)) // DB 저장은 보통 알파 제외

    // + 버튼 누르면 "현재 선택색"을 기준으로 시트 슬라이더 초기화
    fun openSheetWithSelectedColor() {
        val base = selectedName?.let { colorOf(it) }
        if (base != null) {
            r = (base.red * 255f).coerceIn(0f, 255f)
            g = (base.green * 255f).coerceIn(0f, 255f)
            b = (base.blue * 255f).coerceIn(0f, 255f)
            a = base.alpha.coerceIn(0f, 1f)
        }
        newName = ""
        showSheet = true
    }

    // ====== 바깥 “원형 컬러칩 + +버튼” ======
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
                        .border(1.dp, Color(0x22000000), CircleShape)
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

        // ➕ 버튼
        item {
            IconButton(onClick = { openSheetWithSelectedColor() }) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFEFEF))
                        .border(1.dp, Color(0x22000000), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = "색상 추가")
                }
            }
        }
    }

    // ====== “직접 색상 선택” 시트 ======
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                // 상단 헤더 (iOS 느낌)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "직접 색상 선택",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { showSheet = false }) {
                        Icon(Icons.Default.Close, contentDescription = "닫기")
                    }
                }

                Divider()

                Spacer(Modifier.height(12.dp))

                // 미리보기 + HEX
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(preview)
                            .border(1.dp, Color(0x22000000), CircleShape)
                    )
                    Spacer(Modifier.width(12.dp))

                    Column {
                        Text("sRGB 16진수 색상 #", color = Color(0xFF3A7BD5))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.medium)
                                    .border(1.dp, Color(0x33000000), MaterialTheme.shapes.medium)
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(hex.replace("#", ""), style = MaterialTheme.typography.titleMedium)
                            }
                            Spacer(Modifier.width(8.dp))
                            Text("${(a * 100).toInt()}%", color = Color.Gray)
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // 색상 이름
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("색상 이름 (예: 로지핑크)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(14.dp))

                // RGB 슬라이더 (오른쪽 숫자 박스처럼 보여주기)
                ChannelSlider(label = "빨간색", value = r, max = 255f) { r = it }
                ChannelSlider(label = "초록색", value = g, max = 255f) { g = it }
                ChannelSlider(label = "파란색", value = b, max = 255f) { b = it }

                Spacer(Modifier.height(8.dp))

                // Alpha
                ChannelSliderPercent(label = "불투명도", value = a) { a = it }

                Spacer(Modifier.height(16.dp))

                // 하단 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextButton(
                        onClick = { showSheet = false },
                        modifier = Modifier.weight(1f)
                    ) { Text("취소") }

                    Button(
                        onClick = {
                            val key = newName.trim().ifBlank { "사용자색상" }
                            customColors[key] = preview
                            onSelect(key, hex)     // 저장은 HEX(#RRGGBB)
                            showSheet = false
                            newName = ""
                        },
                        enabled = newName.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) { Text("추가") }
                }

                Spacer(Modifier.height(18.dp))
            }
        }
    }
}

@Composable
private fun ChannelSlider(
    label: String,
    value: Float,
    max: Float,
    onChange: (Float) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, modifier = Modifier.width(70.dp))
            Spacer(Modifier.width(8.dp))
            Slider(
                value = value,
                onValueChange = onChange,
                valueRange = 0f..max,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .height(36.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .border(1.dp, Color(0x33000000), MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Text("${value.toInt()}")
            }
        }
    }
}

@Composable
private fun ChannelSliderPercent(
    label: String,
    value: Float,
    onChange: (Float) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, modifier = Modifier.width(70.dp))
            Spacer(Modifier.width(8.dp))
            Slider(
                value = value,
                onValueChange = onChange,
                valueRange = 0f..1f,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .height(36.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .border(1.dp, Color(0x33000000), MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Text("${(value * 100).toInt()}%")
            }
        }
    }
}
