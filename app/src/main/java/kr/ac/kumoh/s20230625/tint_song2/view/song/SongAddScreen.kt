@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package kr.ac.kumoh.s20230625.tint_song2.view.song

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
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
    var rating by remember { mutableStateOf(3f) } // 1~5
    var lyrics by remember { mutableStateOf("") }

    // ✅ 시트가 작아 보이는 문제 해결: 스크롤 + 충분한 높이 확보
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("노래 정보 *")
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("제목") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = singer,
                onValueChange = { singer = it },
                label = { Text("가수") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            Text("선호도 *")
            Spacer(Modifier.height(8.dp))

            // ✅ SwiftUI segmented(1~5) 느낌: Slider + 점수 표시
            Text("${rating.toInt()}점")
            Slider(
                value = rating,
                onValueChange = { rating = it },
                valueRange = 1f..5f,
                steps = 3
            )

            Spacer(Modifier.height(16.dp))
            Text("가사")
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = lyrics,
                onValueChange = { lyrics = it },
                label = { Text("가사") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                singleLine = false
            )

            Spacer(Modifier.height(18.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("취소") }

                Button(
                    onClick = { onAdd(title, singer, rating.toInt(), lyrics.ifBlank { null }) },
                    enabled = title.isNotBlank() && singer.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("추가") }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}
