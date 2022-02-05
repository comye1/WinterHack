package com.comye1.wewon

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.comye1.wewon.navigation.Screen
import com.comye1.wewon.network.Repository
import com.comye1.wewon.network.models.Writing
import com.comye1.wewon.ui.theme.Cream
import com.comye1.wewon.ui.theme.PointBlue
import com.comye1.wewon.ui.theme.WeWonTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val repository = Repository.get()
            WeWonTheme {

                window.statusBarColor = PointBlue.toArgb() // 상단 상태바

                val navController = rememberNavController()

                val startDestination = remember {
                    mutableStateOf(Screen.LogIn.route)
                }

                val (startScreen, setStartScreen) = remember {
                    mutableStateOf(false)
                }

                LaunchedEffect(true) {
                    setStartScreen(true)
                    withTimeout(3000) {
                        if (repository.logInRefresh()) {
                            startDestination.value = Screen.Home.route
                            Log.d("login refresh", "succeed")
                        } else {
                            Log.d("login refresh", "failed")
                        }
                        setStartScreen(false)
                    }
                }

                if (startScreen) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Cream),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "너 문해 너무해",
                            style = MaterialTheme.typography.h3,
                            color = PointBlue,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                } else {
                    /*
   로그인 여부에 따라 startDestination 다르게
    */
                    NavHost(
                        navController = navController,
                        startDestination = startDestination.value
                    ) {
                        composable(Screen.Home.route) {
                            MyDrawer(
                                navController = navController,
                                title = "너 문해 너무해",
                                logOut = { }) {
                                HomeScreen(navController, repository)
                            }
                        }
                        composable(Screen.Random.route) {
                            ReadWritingScreen(
                                navController = navController,
                                random = true,
                                wrt = null,
                                repository = repository
                            )
                        }
                        composable(
                            Screen.Read.route + "/{id}/{title}/{writer}/{url}/{content}/{category}",
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument("title") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                                navArgument("writer") {
                                    type = NavType.StringType
                                    defaultValue = "작가 미상"
                                },
                                navArgument("url") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                                navArgument("content") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                                navArgument("category") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            it.arguments?.let { bundle ->
                                val writing = Writing(
                                    id = bundle.getInt("id"),
                                    title = bundle.getString("title") ?: "",
                                    writer = bundle.getString("writer") ?: "작가 미상",
                                    site_url = bundle.getString("url"),
                                    content = bundle.getString("content") ?: "",
                                    category = bundle.getInt("category")
                                )
                                ReadWritingScreen(
                                    navController = navController,
                                    random = false,
                                    wrt = writing,
                                    repository = repository
                                )
                            }
                        }
                        composable(Screen.Writings.route) {
                            MyDrawer(
                                navController = navController,
                                title = "저장한 글",
                                logOut = { repository.logOut() }) {
                                SavedWritingsScreen(navController, repository)
                            }
                        }
                        composable(Screen.Sentences.route) {
                            MyDrawer(
                                navController = navController,
                                title = "저장한 문장",
                                logOut = { repository.logOut() }) {
                                SavedSentencesScreen(navController, repository)
                            }
                        }
                        composable(Screen.Words.route) {
                            MyDrawer(
                                navController = navController,
                                title = "저장한 단어",
                                logOut = { repository.logOut() }) {
                                SavedWordsScreen(navController, repository)
                            }
                        }
                        composable(Screen.LogIn.route) {
                            LogInScreen(navController, repository)
                        }
                        composable(Screen.SignUp.route) {
                            SignUpScreen(navController)
                        }
                        composable(Screen.Voca.route) {
                            MyDrawer(
                                navController = navController,
                                title = "어휘 모음 ",
                                logOut = { repository.logOut() }) {
                                VocaScreen(navController, repository)
                            }
                        }
                        composable(
                            Screen.Category.route + "/{category}",
                            arguments = listOf(navArgument("category") {
                                type = NavType.IntType
                                defaultValue = -1
                            })
                        ) {
                            val category = it.arguments?.getInt("category")
                            CategoryReading(
                                category = category ?: -1,
                                navController = navController,
                                repository
                            )
                        }
                        composable(Screen.Quiz.route) {
                            QuizScreen(
                                navController = navController,
                                repository = repository
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun MyDrawer(
    navController: NavHostController,
    title: String,
    logOut: () -> Unit,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController) {
                logOut()
                navController.navigate(Screen.LogIn.route) {
                    popUpTo(0) // backStack 모두 제거
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = title)
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
                content()
            }
        }
    )
}