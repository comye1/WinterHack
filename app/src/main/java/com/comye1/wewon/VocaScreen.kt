package com.comye1.wewon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.comye1.wewon.ui.theme.Cream
import com.comye1.wewon.ui.theme.PointBlue
import com.comye1.wewon.util.simpleVerticalScrollbar
import kotlinx.coroutines.launch

@Composable
fun VocaScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController)
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "어휘 모음")
                        }, navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "menu"
                                )

                            }
                        }
                    )
                }
            )
            {
                VocaContent()
            }
        }
    )
}

@Composable
fun VocaContent() {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .simpleVerticalScrollbar(lazyListState)
    ) {
        repeat(20) {
            item {
                VocaItem()
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun VocaItem() {
    Row(Modifier.fillMaxWidth()) {
        Column(
            Modifier
                .fillMaxWidth(.4f)
                .padding(4.dp)
        ) {
            Text(
                text = "단어",
                style = MaterialTheme.typography.h5,
                color = PointBlue,
                modifier =
                Modifier
                    .clip(CircleShape)
                    .background(Cream)
                    .padding(vertical = 4.dp, horizontal = 16.dp)
            )
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "뜻", style = MaterialTheme.typography.h6)
        }
    }
}
