@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package kr.ac.kumoh.s20230625.tint_song2.view.song

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import kr.ac.kumoh.s20230625.tint_song2.navigation.Screens
import kr.ac.kumoh.s20230625.tint_song2.viewmodel.SongViewModel

@Composable
fun SongListScreen(navController: NavHostController, vm: SongViewModel) {
    val songs by vm.songs.collectAsState()
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("노래") },
                actions = {
                    IconButton(onClick = { showAdd = true }) {
                        Icon(Icons.Default.Add, contentDescription = "추가")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            songs.forEach { song ->
                ListItem(
                    headlineContent = { Text(song.title) },
                    supportingContent = { Text(song.singer) },
                    modifier = Modifier.fillMaxWidth(),
                    trailingContent = {
                        TextButton(onClick = {
                            navController.navigate("${Screens.SONG_DETAIL}/${song.id}")
                        }) { Text("보기") }
                    }
                )
                Divider()
            }
        }
    }

    if (showAdd) {
        SongAddScreen(
            onDismiss = { showAdd = false },
            onAdd = { title, singer, rating, lyrics ->
                vm.addSong(title, singer, rating, lyrics)
                showAdd = false
            }
        )
    }
}
