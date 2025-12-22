@file:OptIn(
    androidx.compose.material.ExperimentalMaterialApi::class,
    androidx.compose.material3.ExperimentalMaterial3Api::class
)

package kr.ac.kumoh.s20230625.tint_song2.view.song

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Job
import kr.ac.kumoh.s20230625.tint_song2.navigation.Screens
import kr.ac.kumoh.s20230625.tint_song2.viewmodel.SongViewModel

@Composable
fun SongListScreen(navController: NavHostController, vm: SongViewModel, onOpenDrawer: () -> Job) {
    val songs by vm.songs.collectAsState()
    var showAdd by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { vm.loadSongs() }

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

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(songs, key = { it.id }) { song ->
                val dismissState = rememberDismissState(confirmStateChange = { value ->
                    if (value == DismissValue.DismissedToStart) {
                        vm.deleteSong(song.id)
                        true
                    } else false
                })

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    background = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "삭제",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    dismissContent = {
                        ListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("${Screens.SONG_DETAIL}/${song.id}") },
                            headlineContent = { Text(song.title) },
                            supportingContent = { Text(song.singer) },
                            trailingContent = {
                                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
                            }
                        )
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
