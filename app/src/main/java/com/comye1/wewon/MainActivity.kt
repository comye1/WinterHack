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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.comye1.wewon.ui.theme.Purple700
import com.comye1.wewon.ui.theme.WeWonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeWonTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SignUpScreen()
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
fun LogInScreen() {

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
fun SignUpScreen() {
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
                }
                else {
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