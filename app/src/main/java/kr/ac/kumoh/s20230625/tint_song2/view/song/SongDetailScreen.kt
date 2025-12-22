@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package kr.ac.kumoh.s20230625.tint_song2.view.song

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.ac.kumoh.s20230625.tint_song2.model.Song

@Composable
fun SongDetailScreen(song: Song) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(song.title) }) }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(Modifier.fillMaxWidth()) {
                Text(song.singer, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.weight(1f))
                Text("${song.rating}", color = MaterialTheme.colorScheme.tertiary)
            }
            Spacer(Modifier.height(10.dp))
            Text(song.lyrics ?: "(가사 없음)", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
