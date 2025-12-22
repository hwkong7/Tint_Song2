@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package kr.ac.kumoh.s20230625.tint_song2.view.song

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SongAddScreen(
    onDismiss: () -> Unit,
    onAdd: (String, String, Int, String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var singer by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(3) }
    var lyrics by remember { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp)) {
            Text("노래 정보 *")
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("제목") })
            OutlinedTextField(value = singer, onValueChange = { singer = it }, label = { Text("가수") })

            Spacer(Modifier.height(12.dp))
            Text("선호도 *")
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                (1..5).forEach { score ->
                    FilterChip(
                        selected = rating == score,
                        onClick = { rating = score },
                        label = { Text("${score}점") }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            Text("가사")
            OutlinedTextField(
                value = lyrics,
                onValueChange = { lyrics = it },
                label = { Text("가사") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("취소") }
                Button(
                    onClick = { onAdd(title, singer, rating, lyrics) },
                    enabled = title.isNotBlank() && singer.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) { Text("추가") }
            }
        }
    }
}

