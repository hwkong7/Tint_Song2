package kr.ac.kumoh.s20230625.tint_song2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kr.ac.kumoh.s20230625.tint_song2.model.Song
import kr.ac.kumoh.s20230625.tint_song2.navigation.Screens
import kr.ac.kumoh.s20230625.tint_song2.viewmodel.SongViewModel

@Composable
fun SongListScreen(
    navController: NavHostController,
    vm: SongViewModel
) {
    val songs by vm.songs.collectAsState()
    var showAdd by remember { mutableStateOf(false) }

    // iOS 느낌: 하얀 바탕
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("노래") },
                actions = {
                    TextButton(onClick = { vm.loadSongs() }) { Text("새로고침") }
                    IconButton(onClick = { showAdd = true }) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.background) // 숨김용(자리)
                    }
                    // iOS처럼 +만 두고 싶으면 아래로 교체:
                    TextButton(onClick = { showAdd = true }) { Text("추가") }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(Modifier.padding(padding)) {
            Divider()
            songs.forEach { song ->
                SongRow(
                    song = song,
                    onClick = { navController.navigate("${Screens.SONG_DETAIL}/${song.id}") },
                    onDelete = { vm.deleteSong(song.id) }
                )
                Divider()
            }
        }
    }

    if (showAdd) {
        SongAddSheet(
            onDismiss = { showAdd = false },
            onAdd = { title, singer, rating, lyrics ->
                vm.addSong(title, singer, rating, lyrics)
                showAdd = false
            }
        )
    }
}

@Composable
private fun SongRow(song: Song, onClick: () -> Unit, onDelete: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(song.title, style = MaterialTheme.typography.titleMedium)
            Text(song.singer, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        TextButton(onClick = onClick) { Text("보기") }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "삭제")
        }
    }
}

@Composable
fun SongDetailScreen(song: Song) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(song.title) }) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(song.singer, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.weight(1f))
                Text("${song.rating}", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.tertiary)
            }
            Spacer(Modifier.height(16.dp))
            Text(song.lyrics ?: "(가사 없음)", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongAddSheet(
    onDismiss: () -> Unit,
    onAdd: (title: String, singer: String, rating: Int, lyrics: String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var singer by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(3) }   // Swift 기본 3
    var lyrics by remember { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // iOS Form 느낌: 섹션 헤더
            Text("노래 정보 *", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("제목") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = singer, onValueChange = { singer = it }, label = { Text("가수") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(16.dp))
            Text("선호도 *", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                (1..5).forEach { score ->
                    FilterChip(
                        selected = rating == score,
                        onClick = { rating = score },
                        label = { Text("${score}점") }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("가사", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = lyrics,
                onValueChange = { lyrics = it },
                label = { Text("가사") },
                modifier = Modifier.fillMaxWidth().height(140.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = false
            )

            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(modifier = Modifier.weight(1f), onClick = onDismiss) { Text("취소") }
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = title.isNotBlank() && singer.isNotBlank(),
                    onClick = { onAdd(title.trim(), singer.trim(), rating, lyrics.ifBlank { null }) }
                ) { Text("추가") }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}
