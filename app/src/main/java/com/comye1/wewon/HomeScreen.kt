package com.comye1.wewon

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun HomeScreen() {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent()
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "아직 이름 없음")
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
                HomeContent()
            }
        }
    )
}

@Composable
fun DrawerContent() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Bookmark, contentDescription = "저장한 글")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "저장한 글", style = MaterialTheme.typography.h6)
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                }
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Bookmark, contentDescription = "저장한 문장")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "저장한 문장", style = MaterialTheme.typography.h6)
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Bookmark, contentDescription = "저장한 단어")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "저장한 단어", style = MaterialTheme.typography.h6)
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Logout, contentDescription = "저장한 단어")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "로그아웃", style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
fun HomeContent() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp), verticalArrangement = Arrangement.Center) {
        Button(onClick = { /*TODO*/ }, modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)) {
            Text(text = "랜덤 글 읽기", style = MaterialTheme.typography.h5)
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(Modifier.fillMaxWidth()) {
            Button(onClick = { /*TODO*/ }, modifier = Modifier
                .weight(1f)
                .height(64.dp)) {
                Text(text = "시", style = MaterialTheme.typography.h5)
            }
            Spacer(modifier = Modifier.width(32.dp))
            Button(onClick = { /*TODO*/ }, modifier = Modifier
                .weight(1f)
                .height(64.dp)) {
                Text(text = "소설", style = MaterialTheme.typography.h5)
            }
            Spacer(modifier = Modifier.width(32.dp))
            Button(onClick = { /*TODO*/ }, modifier = Modifier
                .weight(1f)
                .height(64.dp)) {
                Text(text = "수필", style = MaterialTheme.typography.h5)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { /*TODO*/ }, modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)) {
            Text(text = "단어 퀴즈", style = MaterialTheme.typography.h5)
        }
    }
}