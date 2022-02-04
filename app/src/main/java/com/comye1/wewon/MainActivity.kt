package com.comye1.wewon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.comye1.wewon.navigation.Screen
import com.comye1.wewon.network.Writing
import com.comye1.wewon.ui.theme.PointBlue
import com.comye1.wewon.ui.theme.Purple700
import com.comye1.wewon.ui.theme.WeWonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeWonTheme {

                window.statusBarColor = PointBlue.toArgb() // 상단 상태바

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.Home.route) {
                    composable(Screen.Home.route) {
                        HomeScreen(navController)
                    }
                    composable(Screen.Random.route) {
                        ReadWritingScreen(navController = navController, random = true, writing = null)
                    }
                    composable(
                        Screen.Read.route + "/{title}/{writer}/{site_url}/{content}/{category}",
                        arguments = listOf(
                            navArgument("title") {
                                type = NavType.StringType
                                defaultValue = ""
                            },
                            navArgument("writer") {
                                type = NavType.StringType
                                defaultValue = "작가 미상"
                            },
                            navArgument("site_url") {
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
                        it.arguments?.let {
                            val writing = Writing(
                                it.getString("title") ?: "",
                                it.getString("writer") ?: "작가 미상",
                                it.getString("site_url"),
                                it.getString("content") ?: "",
                                it.getInt("category")
                            )
                            ReadWritingScreen(
                                navController = navController,
                                random = false,
                                writing = writing
                            )
                        }

                    }
                    composable(Screen.Writings.route) {
                        SavedWritingsScreen(navController)
                    }
                    composable(Screen.Sentences.route) {
                        SavedSentencesScreen(navController)
                    }
                    composable(Screen.Words.route) {
                        SavedWordsScreen(navController)
                    }
                    composable(Screen.LogIn.route) {
                        LogInScreen(navController)
                    }
                    composable(Screen.SignUp.route) {
                        SignUpScreen(navController)
                    }
                    composable(Screen.Voca.route) {
                        VocaScreen(navController)
                    }
                    composable(
                        Screen.Category.route + "/{category}",
                        arguments = listOf(navArgument("category") {
                            type = NavType.IntType
                            defaultValue = -1
                        })
                    ) {
                        val category = it.arguments?.getInt("category")
                        CategoryReading(category = category ?: -1, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeWonTheme {
        Greeting("Android")
    }
}

@Composable
fun LogInScreen(navController: NavHostController) {

    val (id, setID) = remember {
        mutableStateOf("")
    }

    val (pw, setPW) = remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.8f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = id,
                onValueChange = setID,
                label = { Text(text = "아이디") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = pw,
                onValueChange = setPW,
                label = { Text(text = "비밀번호") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "로그인")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "회원가입")
            }
        }
    }
}

@Composable
fun SignUpScreen(navController: NavHostController) {
    val (id, setID) = remember {
        mutableStateOf("")
    }

    val (pw, setPW) = remember {
        mutableStateOf("")
    }

    val (pwAgain, setPWAgain) = remember {
        mutableStateOf("")
    }

    val (notDuplicated, setNotDuplicated) = remember {
        mutableStateOf(false)
    }

    val (duplicationCheckerShown, showDuplicationChecker) = remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.8f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = id,
                onValueChange = {
                    setID(it)
                    setNotDuplicated(false)
                },
                label = { Text(text = "아이디") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (notDuplicated) Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "not duplicated",
                        tint = Purple700
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    showDuplicationChecker(true)
                    /*TODO*/
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = (!notDuplicated) && id.isNotBlank()
            ) {
                if (notDuplicated) {
                    Text(text = "아이디 중복 확인 완료")
                } else {
                    Text(text = "아이디 중복 확인")
                }
            }
            if (duplicationCheckerShown) {
                Dialog(onDismissRequest = {
                    showDuplicationChecker(false)
                    setNotDuplicated(true)
                }) {
                    Column(
                        Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(buildAnnotatedString {
                            append("아이디 ")
                            pushStyle(SpanStyle(color = Purple700))
                            append(id)
                            pop()
                            append("를 사용할 수 있습니다.")
                        })
                        Spacer(modifier = Modifier.height(32.dp))
                        OutlinedButton(
                            onClick = {
                                showDuplicationChecker(false)
                                setNotDuplicated(true)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "확인")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = pw,
                onValueChange = setPW,
                label = { Text(text = "비밀번호") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = pwAgain,
                onValueChange = setPWAgain,
                label = { Text(text = "비밀번호 확인") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                trailingIcon = {
                    if (pw.isNotBlank() && (pw == pwAgain))
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "password confirmed",
                            tint = Purple700
                        )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth(),
                enabled = notDuplicated && (pw.isNotBlank() && (pw == pwAgain))
            ) {
                Text(text = "회원가입")
            }
        }
    }
}

