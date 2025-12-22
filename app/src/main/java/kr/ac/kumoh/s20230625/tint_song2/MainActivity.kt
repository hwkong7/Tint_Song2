package kr.ac.kumoh.s20230625.tint_song2

import androidx.compose.foundation.layout.padding
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import kr.ac.kumoh.s20230625.tint_song2.navigation.Screens
import kr.ac.kumoh.s20230625.tint_song2.view.song.SongDetailScreen
import kr.ac.kumoh.s20230625.tint_song2.view.song.SongListScreen
import kr.ac.kumoh.s20230625.tint_song2.view.tint.TintDetailScreen
import kr.ac.kumoh.s20230625.tint_song2.view.tint.TintListScreen
import kr.ac.kumoh.s20230625.tint_song2.view.song.SongListScreen
import kr.ac.kumoh.s20230625.tint_song2.viewmodel.SongViewModel
import kr.ac.kumoh.s20230625.tint_song2.viewmodel.TintViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen(
    songVm: SongViewModel = viewModel(),
    tintVm: TintViewModel = viewModel()
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val route = backStackEntry?.destination?.route ?: Screens.TINT

    androidx.compose.material3.Scaffold(
        bottomBar = {
            BottomBar(
                currentRoute = route,
                onNavigate = { target ->
                    navController.navigate(target) {
                        launchSingleTop = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screens.TINT,
            modifier = androidx.compose.ui.Modifier.padding(padding)
        ) {
            composable(Screens.TINT) {
                TintListScreen(navController, tintVm)
            }
            composable(Screens.SONG) {
                SongListScreen(navController, songVm)
            }
            composable(Screens.SONG_DETAIL_ROUTE) { entry ->
                val id = entry.arguments?.getString(Screens.ID_ARG) ?: return@composable
                val song = songVm.findSong(id) ?: return@composable
                SongDetailScreen(song)
            }
            composable(Screens.TINT_DETAIL_ROUTE) { entry ->
                val id = entry.arguments?.getString(Screens.ID_ARG) ?: return@composable
                val tint = tintVm.findTint(id) ?: return@composable
                TintDetailScreen(tint)
            }
        }
    }
}

@Composable
private fun BottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val selectedTab = when {
        currentRoute.startsWith(Screens.TINT) || currentRoute.startsWith(Screens.TINT_DETAIL) -> Screens.TINT
        else -> Screens.SONG
    }

    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == Screens.TINT,
            onClick = { onNavigate(Screens.TINT) },
            icon = { Text("💄") },
            label = { Text("Tint") }
        )
        NavigationBarItem(
            selected = selectedTab == Screens.SONG,
            onClick = { onNavigate(Screens.SONG) },
            icon = { Text("🎵") },
            label = { Text("Songs") }
        )
    }
}

