package kr.ac.kumoh.s20230625.tint_song2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import kr.ac.kumoh.s20230625.tint_song2.navigation.Screens
import kr.ac.kumoh.s20230625.tint_song2.view.song.SongDetailScreen
import kr.ac.kumoh.s20230625.tint_song2.view.song.SongListScreen
import kr.ac.kumoh.s20230625.tint_song2.view.tint.TintDetailScreen
import kr.ac.kumoh.s20230625.tint_song2.view.tint.TintListScreen
import kr.ac.kumoh.s20230625.tint_song2.viewmodel.SongViewModel
import kr.ac.kumoh.s20230625.tint_song2.viewmodel.TintViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainScreen() }
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

    val selectedTab = when {
        route.startsWith(Screens.TINT) -> Screens.TINT
        else -> Screens.SONG
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun navigateTo(target: String) {
        navController.navigate(target) {
            launchSingleTop = true
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            restoreState = true
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Tint_Song", style = MaterialTheme.typography.titleLarge, modifier = androidx.compose.ui.Modifier.padding(16.dp))

                NavigationDrawerItem(
                    label = { Text("💄 Tint") },
                    selected = selectedTab == Screens.TINT,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navigateTo(Screens.TINT)
                    }
                )

                NavigationDrawerItem(
                    label = { Text("🎵 Songs") },
                    selected = selectedTab == Screens.SONG,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navigateTo(Screens.SONG)
                    }
                )

                Divider(modifier = androidx.compose.ui.Modifier.padding(vertical = 8.dp))

                NavigationDrawerItem(
                    label = { Text("🔄 새로고침") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        if (selectedTab == Screens.TINT) tintVm.loadTints() else songVm.loadSongs()
                    }
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                BottomBar(
                    currentRoute = route,
                    onNavigate = { target -> navigateTo(target) }
                )
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Screens.TINT,
                modifier = androidx.compose.ui.Modifier.padding(padding)
            ) {
                composable(Screens.TINT) {
                    TintListScreen(
                        navController = navController,
                        vm = tintVm,
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                }
                composable(Screens.SONG) {
                    SongListScreen(
                        navController = navController,
                        vm = songVm,
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                }

                // detail
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
}

@Composable
private fun BottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val selectedTab = when {
        currentRoute.startsWith(Screens.TINT) -> Screens.TINT
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
