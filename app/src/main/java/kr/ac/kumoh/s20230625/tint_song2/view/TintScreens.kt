package kr.ac.kumoh.s20230625.tint_song2.view

import androidx.compose.foundation.border
import kotlin.collections.sorted

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kr.ac.kumoh.s20230625.tint_song2.model.Tint
import kr.ac.kumoh.s20230625.tint_song2.navigation.Screens
import kr.ac.kumoh.s20230625.tint_song2.util.ColorHex
import kr.ac.kumoh.s20230625.tint_song2.util.tintColorHexMap
import kr.ac.kumoh.s20230625.tint_song2.util.tintColors
import kr.ac.kumoh.s20230625.tint_song2.viewmodel.TintViewModel
import kotlin.math.roundToInt

@Composable
fun TintListScreen(
    navController: NavHostController,
    vm: TintViewModel
) {
    val tints by vm.tints.collectAsState()
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("틴트") },
                actions = {
                    TextButton(onClick = { vm.loadTints() }) { Text("새로고침") }
                    TextButton(onClick = { showAdd = true }) { Text("추가") }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(Modifier.padding(padding)) {
            Divider()
            tints.forEach { tint ->
                TintRow(
                    tint = tint,
                    onClick = { navController.navigate("${Screens.TINT_DETAIL}/${tint.id}") },
                    onDelete = { vm.deleteTint(tint.id) }
                )
                Divider()
            }
        }
    }

    if (showAdd) {
        TintAddSheet(
            onDismiss = { showAdd = false },
            onAdd = { productName, brand, colorFamily, colorHex, rating, description ->
                vm.addTint(productName, brand, colorFamily, colorHex, rating, description)
                showAdd = false
            }
        )
    }
}

@Composable
private fun TintRow(tint: Tint, onClick: () -> Unit, onDelete: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(tint.productName, style = MaterialTheme.typography.titleMedium)
            Text(tint.brand, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        TextButton(onClick = onClick) { Text("보기") }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "삭제")
        }
    }
}

@Composable
fun TintDetailScreen(tint: Tint) {
    val circleColor: Color =
        tint.colorHex?.let { ColorHex.fromHex(it) }
            ?: tint.colorFamily?.let { tintColors[it] }
            ?: Color.LightGray

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(tint.productName) }) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(tint.productName, style = MaterialTheme.typography.headlineMedium)
            Text(tint.brand, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(42.dp).clip(CircleShape).background(circleColor)
                )
                Spacer(Modifier.width(12.dp))
                Text(tint.colorFamily ?: "-", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(16.dp))
            Text("⭐️ ${tint.rating}점", style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.height(16.dp))
            Text(tint.description ?: "(설명 없음)", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TintAddSheet(
    onDismiss: () -> Unit,
    onAdd: (
        productName: String,
        brand: String,
        colorFamily: String?,
        colorHex: String?,
        rating: Int,
        description: String?
    ) -> Unit
) {
    var productName by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var colorFamily by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(5) }
    var description by remember { mutableStateOf("") }

    // 사용자 색상 추가(이름→Color)
    val customColors = remember { mutableStateMapOf<String, Color>() }

    var showColorPicker by remember { mutableStateOf(false) }
    var newColorName by remember { mutableStateOf("") }
    var r by remember { mutableFloatStateOf(255f) }
    var g by remember { mutableFloatStateOf(105f) }
    var b by remember { mutableFloatStateOf(180f) }

    var selectedHex by remember { mutableStateOf<String?>(null) }

    val allKeys = remember(customColors.size) {
        (tintColors.keys + customColors.keys).toList().sorted()
    }

    fun currentPickedColor(): Color {
        return Color(r / 255f, g / 255f, b / 255f, 1f)
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("제품 정보 *", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = productName, onValueChange = { productName = it }, label = { Text("제품명") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("브랜드") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(16.dp))
            Text("컬러 선택", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                items(allKeys) { key ->
                    val color = customColors[key] ?: tintColors[key] ?: Color.LightGray
                    val selected = colorFamily == key

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(
                            Modifier.size(36.dp).clip(CircleShape).background(color)
                        )
                        if (selected) {
                            Box(
                                Modifier.size(42.dp).clip(CircleShape)
                                    .background(Color.Transparent)
                                    .border(2.dp, Color.Black, CircleShape)
                            )
                        }
                    }

                    Spacer(Modifier.width(0.dp))
                    // 클릭 영역
                    Box(
                        Modifier
                            .size(42.dp)
                            .padding(0.dp)
                    ) {
                        // (겹침 방지용 더미)
                    }
                }

                item {
                    IconButton(onClick = { showColorPicker = true }) {
                        Box(Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFEFEFEF)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Add, contentDescription = "색상 추가")
                        }
                    }
                }
            }

            // LazyRow 아이템 클릭 처리(간단하게 아래에 별도 버튼으로 처리)
            Spacer(Modifier.height(8.dp))
            FlowRowColors(
                keys = allKeys,
                selectedKey = colorFamily,
                customColors = customColors,
                onPick = { key ->
                    colorFamily = key
                    selectedHex = tintColorHexMap[key] ?: customColors[key]?.let { ColorHex.toHex(it) }
                }
            )

            if (showColorPicker) {
                Spacer(Modifier.height(12.dp))
                Text("직접 색상 추가", style = MaterialTheme.typography.titleSmall)

                Spacer(Modifier.height(8.dp))
                SliderRow("R", r) { r = it }
                SliderRow("G", g) { g = it }
                SliderRow("B", b) { b = it }

                val preview = currentPickedColor()
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(28.dp).clip(CircleShape).background(preview))
                    Spacer(Modifier.width(10.dp))
                    Text(ColorHex.toHex(preview))
                }

                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = newColorName,
                    onValueChange = { newColorName = it },
                    label = { Text("색상 이름 입력 (예: 로지핑크)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (newColorName.isBlank()) return@Button
                        val c = preview
                        customColors[newColorName.trim()] = c

                        colorFamily = newColorName.trim()
                        selectedHex = ColorHex.toHex(c)

                        newColorName = ""
                        showColorPicker = false
                    }
                ) { Text("색상 추가") }
            }

            Spacer(Modifier.height(16.dp))
            Text("평점", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))

            // Swift Picker(1..10)
            var expand by remember { mutableStateOf(false) }
            OutlinedButton(onClick = { expand = true }) { Text("${rating}점") }
            DropdownMenu(expanded = expand, onDismissRequest = { expand = false }) {
                (1..10).forEach {
                    DropdownMenuItem(
                        text = { Text("${it}점") },
                        onClick = { rating = it; expand = false }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("설명", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("설명") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = false
            )

            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(modifier = Modifier.weight(1f), onClick = onDismiss) { Text("취소") }
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = productName.isNotBlank() && brand.isNotBlank() && colorFamily.isNotBlank(),
                    onClick = {
                        onAdd(
                            productName.trim(),
                            brand.trim(),
                            colorFamily.trim(),
                            selectedHex,
                            rating,
                            description.ifBlank { null }
                        )
                    }
                ) { Text("추가") }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SliderRow(label: String, value: Float, onChange: (Float) -> Unit) {
    Column {
        Text("$label: ${value.roundToInt()}")
        Slider(value = value, onValueChange = onChange, valueRange = 0f..255f)
    }
}

/**
 * LazyRow 클릭 처리를 깔끔하게 하기 위해, iOS처럼 “색 원형들”을 한번 더 그려서 탭 선택 처리.
 * (UI는 심플하게 유지하면서 동작은 Swift랑 같게)
 */
@Composable
private fun FlowRowColors(
    keys: List<String>,
    selectedKey: String,
    customColors: Map<String, Color>,
    onPick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // 단순하게 3개씩 줄바꿈 느낌
        val chunk = keys.chunked(4)
        chunk.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { key ->
                    val c = customColors[key] ?: tintColors[key] ?: Color.LightGray
                    val selected = selectedKey == key
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            Modifier.size(34.dp).clip(CircleShape).background(c)
                        )
                        if (selected) {
                            Box(
                                Modifier.size(40.dp).clip(CircleShape)
                                    .border(2.dp, Color.Black, CircleShape)
                            )
                        }
                    }
                    Spacer(Modifier.width(0.dp))
                    TextButton(onClick = { onPick(key) }) { Text(key) }
                }
            }
        }
    }
}
