package com.comye1.wewon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.comye1.wewon.navigation.Screen
import com.comye1.wewon.network.Repository
import com.comye1.wewon.ui.theme.Cream
import com.comye1.wewon.ui.theme.PointBlue


@Composable
fun HomeScreen(navController: NavHostController, repository: Repository) {
    HomeContent(navController)
}

@Composable
fun DrawerContent(navController: NavHostController, logOut: () -> Unit) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (currentRoute == Screen.Home.route) Cream else Color.White)
                .clickable {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
                    }
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Home, contentDescription = "홈")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "홈", style = MaterialTheme.typography.h6)
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (currentRoute == Screen.Writings.route) Cream else Color.White)
                .clickable {
                    navController.navigate(Screen.Writings.route) {
                        popUpTo(Screen.Home.route)
                    }
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
                .background(if (currentRoute == Screen.Sentences.route) Cream else Color.White)
                .clickable {
                    navController.navigate(Screen.Sentences.route) {
                        popUpTo(Screen.Home.route)
                    }
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
                .background(if (currentRoute == Screen.Words.route) Cream else Color.White)
                .clickable {
                    navController.navigate(Screen.Words.route) {
                        popUpTo(Screen.Home.route)
                    }
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
                .background(if (currentRoute == Screen.Voca.route) Cream else Color.White)
                .clickable {
                    navController.navigate(Screen.Voca.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Book, contentDescription = "어휘 모음")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "어휘 모음", style = MaterialTheme.typography.h6)
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    logOut()
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Logout, contentDescription = "로그아웃")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "로그아웃", style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
fun HomeContent(navController: NavHostController) {

    val buttonColors = ButtonDefaults.textButtonColors(
        backgroundColor = Cream,
        contentColor = PointBlue
    )

    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp), verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                navController.navigate(Screen.Random.route)
            }, modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = buttonColors
        ) {
            Text(text = "랜덤 글 읽기", style = MaterialTheme.typography.h5)
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    navController.navigate(Screen.Category.route + "/1")
                }, modifier = Modifier
                    .weight(1f)
                    .height(64.dp),
                colors = buttonColors
            ) {
                Text(text = "시", style = MaterialTheme.typography.h5)
            }
            Spacer(modifier = Modifier.width(32.dp))
            Button(
                onClick = {
                    navController.navigate(Screen.Category.route + "/2")
                }, modifier = Modifier
                    .weight(1f)
                    .height(64.dp),
                colors = buttonColors
            ) {
                Text(text = "소설", style = MaterialTheme.typography.h5)
            }
            Spacer(modifier = Modifier.width(32.dp))
            Button(
                onClick = {
                    navController.navigate(Screen.Category.route + "/3")
                }, modifier = Modifier
                    .weight(1f)
                    .height(64.dp),
                colors = buttonColors
            ) {
                Text(text = "수필", style = MaterialTheme.typography.h5)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                navController.navigate(Screen.Quiz.route)
            }, modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = buttonColors
        ) {
            Text(text = "단어 퀴즈", style = MaterialTheme.typography.h5)
        }
    }
}