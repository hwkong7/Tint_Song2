package kr.ac.kumoh.s20230625.tint_song2.view.tint

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
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
import kotlin.math.roundToInt

@Composable
fun AdvancedColorPicker(
    onSelect: (Color) -> Unit
) {
    var mode by remember { mutableStateOf("스펙트럼") }
    var color by remember { mutableStateOf(Color.Red) }
    var alpha by remember { mutableFloatStateOf(1f) }
    val favorites = remember { mutableStateListOf<Color>() }

    // 스펙트럼 클릭 위치 저장 (마우스 포인터 역할)
    var pointerPos by remember { mutableStateOf<Offset?>(null) }

    Column(Modifier.padding(16.dp)) {
        // ---------- 상단 모드 탭 ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val modes = listOf("격자", "스펙트럼", "슬라이더")
            modes.forEachIndexed { index, name ->
                Text(
                    text = name,
                    color = if (mode == name) Color.Black else Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .clickable { mode = name }
                        .padding(horizontal = 8.dp)
                )
                if (index < modes.lastIndex) {
                    Text(" | ", color = Color.LightGray)
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // ---------- 색상 선택 영역 ----------
        when (mode) {
            "격자" -> ColorGrid { color = it }
            "스펙트럼" -> SpectrumCanvas(
                color = color,
                pointerPos = pointerPos,
                onPick = { c, pos ->
                    color = c
                    pointerPos = pos
                }
            )
            "슬라이더" -> RGBSliderPicker { color = it }
        }

        Spacer(Modifier.height(20.dp))

        // ---------- 불투명도 ----------
        Text("불투명도: ${(alpha * 100).toInt()}%")
        Slider(
            value = alpha,
            onValueChange = { alpha = it },
            valueRange = 0f..1f
        )

        Spacer(Modifier.height(12.dp))

        // ---------- HEX 및 미리보기 ----------
        val current = color.copy(alpha = alpha)
        val hex = "#%06X".format(0xFFFFFF and current.toArgb())
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(current)
                    .border(1.dp, Color.Gray, CircleShape)
            )
            Spacer(Modifier.width(8.dp))
            Text(hex, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(16.dp))

        // ---------- 최근 색상 ----------
        Text("최근 색상", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            favorites.forEach { c ->
                Box(
                    Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(c)
                        .clickable { color = c }
                )
            }
            IconButton(onClick = {
                if (!favorites.contains(color)) favorites.add(color)
            }) {
                Icon(Icons.Default.Add, contentDescription = "색상 추가")
            }
        }

        Spacer(Modifier.height(24.dp))

        // ---------- 선택 완료 버튼 ----------
        Button(
            onClick = { onSelect(current) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("선택 완료")
        }
    }
}

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

        // 🔹 클릭한 위치 표시 (하얀 포인터)
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
        Slider(
            value = r,
            onValueChange = {
                r = it
                onPick(Color(r / 255f, g / 255f, b / 255f))
            },
            valueRange = 0f..255f
        )

        Text("G: ${g.toInt()}")
        Slider(
            value = g,
            onValueChange = {
                g = it
                onPick(Color(r / 255f, g / 255f, b / 255f))
            },
            valueRange = 0f..255f
        )

        Text("B: ${b.toInt()}")
        Slider(
            value = b,
            onValueChange = {
                b = it
                onPick(Color(r / 255f, g / 255f, b / 255f))
            },
            valueRange = 0f..255f
        )
    }
}
