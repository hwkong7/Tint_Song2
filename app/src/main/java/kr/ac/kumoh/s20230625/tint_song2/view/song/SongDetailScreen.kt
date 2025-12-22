package kr.ac.kumoh.s20230625.tint_song2.view.song

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kr.ac.kumoh.s20230625.tint_song2.model.Song

@Composable
fun SongDetailScreen(song: Song) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = song.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.padding(6.dp))

        Text(
            text = song.singer,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.padding(10.dp))

        Text(
            text = "⭐ ${song.rating}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.padding(14.dp))

        Text(
            text = song.lyrics ?: "(가사 없음)",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
