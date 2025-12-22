package kr.ac.kumoh.s20230625.tint_song2.view.tint

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TintColorPicker(
    tintColors: Map<String, Color>,
    selected: String?,
    onSelect: (String, Color) -> Unit
) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        tintColors.forEach { (name, color) ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { onSelect(name, color) },
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                if (selected == name) {
                    Box(
                        Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Black, CircleShape)
                    )
                }
            }
        }
    }
}
