@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package kr.ac.kumoh.s20230625.tint_song2.view.tint

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import kr.ac.kumoh.s20230625.tint_song2.navigation.Screens
import kr.ac.kumoh.s20230625.tint_song2.viewmodel.TintViewModel

@Composable
fun TintListScreen(navController: NavHostController, vm: TintViewModel) {
    val tints by vm.tints.collectAsState()
    var showAdd by remember { mutableStateOf(false) }

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
        Column(Modifier.padding(padding)) {
            tints.forEach { tint ->
                ListItem(
                    headlineContent = { Text(tint.productName) },
                    supportingContent = { Text(tint.brand) },
                    modifier = Modifier.fillMaxWidth(),
                    trailingContent = {
                        TextButton(onClick = {
                            navController.navigate("${Screens.TINT_DETAIL}/${tint.id}")
                        }) { Text("보기") }
                    }
                )
                Divider()
            }
        }
    }

    if (showAdd) {
        TintAddScreen(
            onDismiss = { showAdd = false },
            onAdd = { name, brand, color, hex, rating, desc ->
                vm.addTint(name, brand, color, hex, rating, desc)
                showAdd = false
            }
        )
    }
}
