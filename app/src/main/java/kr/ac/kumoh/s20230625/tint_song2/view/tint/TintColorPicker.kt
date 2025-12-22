@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package kr.ac.kumoh.s20230625.tint_song2.view.tint

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kr.ac.kumoh.s20230625.tint_song2.util.ColorHex
import kotlin.math.roundToInt

@Composable
fun TintColorPicker(
    baseColors: Map<String, Color>,
    customColors: MutableMap<String, Color>,
    selectedName: String?,
    onSelect: (name: String, hex: String) -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }

    // 기본값
    var selectedColor by remember { mutableStateOf(Color(0xFFFF69B4)) } // 핫핑크
    var newName by remember { mutableStateOf("") }

    val keys = (baseColors.keys + customColors.keys).distinct().sorted()

    // 원형 칩 + 추가 버튼
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(keys) { key ->
            val color = customColors[key] ?: baseColors[key] ?: Color.LightGray
            val selected = selectedName == key

            Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                Box(
                    Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(1.dp, Color(0x22000000), CircleShape)
                        .clickable { onSelect(key, ColorHex.toHex(color)) }
                )
                if (selected) {
                    Box(
                        Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Black, CircleShape)
                    )
                }
            }
        }

        // ➕ 버튼
        item {
            IconButton(onClick = { showSheet = true }) {
                Box(
                    Modifier
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

    // ======= “직접 색상 선택” 시트 =======
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            containerColor = Color.White
        ) {
            var mode by remember { mutableStateOf("스펙트럼") }
            var pointerPos by remember { mutableStateOf<Offset?>(null) }
            var alpha by remember { mutableFloatStateOf(1f) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                // ---------- 헤더 ----------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("직접 색상 선택", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { showSheet = false }) {
                        Icon(Icons.Default.Close, contentDescription = "닫기", tint = Color.Gray)
                    }
                }
                Divider()

                Spacer(Modifier.height(8.dp))

                // ---------- 모드 탭 ----------
                val modes = listOf("격자", "스펙트럼", "슬라이더")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .background(Color(0xFFF6F6F6), MaterialTheme.shapes.medium),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    modes.forEachIndexed { index, name ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable { mode = name }
                                .background(if (mode == name) Color.White else Color.Transparent)
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name,
                                color = if (mode == name) Color.Black else Color.Gray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        if (index < modes.lastIndex) {
                            Box(
                                Modifier
                                    .width(1.dp)
                                    .fillMaxHeight(0.5f)
                                    .background(Color.LightGray)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ---------- 선택 모드별 UI ----------
                when (mode) {
                    "격자" -> ColorGrid { selectedColor = it }
                    "스펙트럼" -> SpectrumCanvas(selectedColor, pointerPos) { c, pos ->
                        selectedColor = c
                        pointerPos = pos
                    }
                    "슬라이더" -> RGBSliderPicker { selectedColor = it }
                }

                Spacer(Modifier.height(16.dp))

                // ---------- 미리보기 ----------
                val current = selectedColor.copy(alpha = alpha)
                val hex = "#%06X".format(0xFFFFFF and current.toArgb())

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(current)
                            .border(1.dp, Color.Gray, CircleShape)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(hex, style = MaterialTheme.typography.titleMedium)
                }

                Spacer(Modifier.height(12.dp))
                Text("불투명도: ${(alpha * 100).toInt()}%")
                Slider(value = alpha, onValueChange = { alpha = it }, valueRange = 0f..1f)

                Spacer(Modifier.height(16.dp))

                // ---------- 이름 입력 ----------
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("색상 이름 (예: 로지핑크)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                // ---------- 하단 버튼 ----------
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
                            customColors[key] = current
                            onSelect(key, hex)
                            showSheet = false
                        },
                        enabled = newName.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) { Text("추가") }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// ===================== 서브 컴포저블 =====================
@Composable
private fun ColorGrid(onPick: (Color) -> Unit) {
    val palette = listOf(
        Color.Red, Color.Yellow, Color.Blue, Color.Green,
        Color.Magenta, Color.Cyan, Color(0xFFFFA500), Color(0xFF8B00FF),
        Color.Black, Color.Gray, Color.LightGray, Color.White
    )
    LazyVerticalGrid(columns = GridCells.Fixed(6), modifier = Modifier.height(200.dp)) {
        items(palette) { c ->
            Box(
                Modifier
                    .size(44.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(c)
                    .border(1.dp, Color.LightGray, CircleShape)
                    .clickable { onPick(c) }
            )
        }
    }
}

@Composable
private fun SpectrumCanvas(
    color: Color,
    pointerPos: Offset?,
    onPick: (Color, Offset) -> Unit
) {
    Canvas(
        Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(MaterialTheme.shapes.medium)
            .border(1.dp, Color.LightGray)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val h = offset.x / size.width * 360f
                    val v = 1 - offset.y / size.height
                    val c = Color.hsv(h, 1f, v.coerceIn(0f, 1f))
                    onPick(c, offset)
                }
            }
    ) {
        for (x in 0 until size.width.roundToInt() step 4) {
            for (y in 0 until size.height.roundToInt() step 4) {
                val h = x / size.width * 360
                val v = 1 - (y / size.height)
                drawRect(
                    color = Color.hsv(h.toFloat(), 1f, v.coerceIn(0f, 1f)),
                    topLeft = Offset(x.toFloat(), y.toFloat()),
                    size = Size(4f, 4f)
                )
            }
        }

        pointerPos?.let { pos ->
            drawCircle(
                color = Color.White,
                radius = 10f,
                center = pos,
                style = Stroke(width = 2f)
            )
        }
    }
}

@Composable
private fun RGBSliderPicker(onPick: (Color) -> Unit) {
    var r by remember { mutableFloatStateOf(255f) }
    var g by remember { mutableFloatStateOf(120f) }
    var b by remember { mutableFloatStateOf(120f) }

    Column {
        Text("R: ${r.toInt()}")
        Slider(value = r, onValueChange = {
            r = it; onPick(Color(r / 255f, g / 255f, b / 255f))
        }, valueRange = 0f..255f)

        Text("G: ${g.toInt()}")
        Slider(value = g, onValueChange = {
            g = it; onPick(Color(r / 255f, g / 255f, b / 255f))
        }, valueRange = 0f..255f)

        Text("B: ${b.toInt()}")
        Slider(value = b, onValueChange = {
            b = it; onPick(Color(r / 255f, g / 255f, b / 255f))
        }, valueRange = 0f..255f)
    }
}
