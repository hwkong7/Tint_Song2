@file:OptIn(
    androidx.compose.material.ExperimentalMaterialApi::class,
    androidx.compose.material3.ExperimentalMaterial3Api::class
)

package kr.ac.kumoh.s20230625.tint_song2.view.tint

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
import kr.ac.kumoh.s20230625.tint_song2.viewmodel.TintViewModel

@Composable
fun TintListScreen(navController: NavHostController, vm: TintViewModel, onOpenDrawer: () -> Job) {
    val tints by vm.tints.collectAsState()
    var showAdd by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { vm.loadTints() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("틴트") },
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
            items(tints, key = { it.id }) { tint ->
                val dismissState = rememberDismissState(confirmStateChange = { value ->
                    if (value == DismissValue.DismissedToStart) {
                        vm.deleteTint(tint.id)
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
                            Icon(Icons.Default.Delete, contentDescription = "삭제", tint = MaterialTheme.colorScheme.error)
                        }
                    },
                    dismissContent = {
                        ListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("${Screens.TINT_DETAIL}/${tint.id}") },
                            headlineContent = { Text(tint.productName) },
                            supportingContent = { Text(tint.brand) },
                            trailingContent = { Icon(Icons.Default.KeyboardArrowRight, contentDescription = null) }
                        )
                    }
                )
                Divider()
            }
        }
    }

    if (showAdd) {
        TintAddScreen(
            onDismiss = { showAdd = false },
            onAdd = { name, brand, colorFamily, colorHex, rating, desc ->
                vm.addTint(name, brand, colorFamily, colorHex, rating, desc)
                showAdd = false
            }
        )
    }
}
