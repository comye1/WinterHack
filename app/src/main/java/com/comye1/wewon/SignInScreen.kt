package com.comye1.wewon

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.comye1.wewon.navigation.Screen
import com.comye1.wewon.network.Repository
import com.comye1.wewon.network.WeWonApi
import com.comye1.wewon.network.models.LogInBody
import com.comye1.wewon.network.models.LogInResponse
import com.comye1.wewon.ui.theme.Purple700
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Composable
fun LogInScreen(navController: NavHostController, repository: Repository) {

    val (id, setID) = remember {
        mutableStateOf("")
    }

    val (pw, setPW) = remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
            Button(onClick = {
                scope.launch {
                    logIn(
                        id,
                        pw,
                        {
                            navController.navigate(Screen.Home.route)
                        },
                        {
                            Toast.makeText(context, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        },
                        { repository.saveToken(it) })
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "로그인")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                navController.navigate(Screen.SignUp.route)
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "회원가입")
            }
        }
    }
}

suspend fun logIn(
    id: String,
    pw: String,
    onSuccess: () -> Unit,
    onFailures: () -> Unit,
    saveToken: (LogInResponse) -> Unit
) {
    try {
        val logInResponse =
            WeWonApi.retrofitService.logIn(
                LogInBody(id, pw)
            )
        if (logInResponse.token != "invalid token") {
            saveToken(logInResponse)
            onSuccess()
            return
        }
    } catch (e: Exception) {
        Log.d("retrofit", "login ${e.message}")
        onFailures()
        return
    }
//        .enqueue(object : Callback<LogInResponse> {
//        override fun onResponse(call: Call<LogInResponse>, response: Response<LogInResponse>) {
//            if (response.isSuccessful && response.body() != null) {
//                onSuccess()
//                response.body()?.let {
//                    saveToken(it)
//                }
//            }
//        }
//
//        override fun onFailure(call: Call<LogInResponse>, t: Throwable) {
//            onFailures()
//        }
//    })
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

    val (signUpStatus, signUpSucceed) = remember {
        mutableStateOf(-1)
        /*
        -1, 아직 요청하지 않음
        0, 아이디가 중복됨
        1, 성공
         */
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
                onClick = {
                    // 회원가입 및 로그인 화면으로 이동
                    scope.launch {
                        signUp(id, pw, {
                            navController.navigate(Screen.LogIn.route)
                        }, {
                            Toast.makeText(context, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        })
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = notDuplicated && (pw.isNotBlank() && (pw == pwAgain))
            ) {
                Text(text = "회원가입")
            }
        }
    }
}

suspend fun idCheck(
    id: String,
    pw: String,
    onSuccess: () -> Unit,
    onFailures: () -> Unit
) {

}

suspend fun signUp(
    id: String,
    pw: String,
    onSuccess: () -> Unit,
    onFailures: () -> Unit,
) {
    try {
        WeWonApi.retrofitService.signUp(
            LogInBody(id, pw)
        ).let {
            when (it.status) {
                "200" -> {
                    onSuccess()
                }
                "400" -> {
                    onFailures()
                }
            }
            return
        }

    } catch (e: Exception) {
        Log.d("retrofit", "login ${e.message}")
        onFailures()
        return
    }
//        .enqueue(object : Callback<SignUpResponse> {
//        override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
//            if (response.isSuccessful && response.body() != null) {
//                response.body()!!.let {
//                    when (it.status) {
//                        "200" -> {
//                            onSuccess()
//                        }
//                        "400" -> {
//                            onFailures()
//                        }
//                    }
//                }
//            }
//        }
//
//        override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
//            onFailures()
//        }
//    })
}