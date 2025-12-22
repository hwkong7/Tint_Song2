@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package kr.ac.kumoh.s20230625.tint_song2.view.tint

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kr.ac.kumoh.s20230625.tint_song2.model.Tint
import kr.ac.kumoh.s20230625.tint_song2.util.ColorHex
import kr.ac.kumoh.s20230625.tint_song2.util.tintColors

@Composable
fun TintDetailScreen(tint: Tint) {
    val color = tint.colorHex?.let { ColorHex.fromHex(it) }
        ?: tint.colorFamily?.let { tintColors[it] }
        ?: Color.LightGray

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text(tint.productName) }) }) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(tint.productName, style = MaterialTheme.typography.headlineMedium)
            Text(tint.brand, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(Modifier.height(14.dp))
            Row {
                Box(Modifier.size(42.dp).clip(CircleShape).background(color))
                Spacer(Modifier.width(12.dp))
                Text(tint.colorFamily ?: "-", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(14.dp))
            Text("⭐️ ${tint.rating}점", style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.height(14.dp))
            Text(tint.description ?: "(설명 없음)", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
