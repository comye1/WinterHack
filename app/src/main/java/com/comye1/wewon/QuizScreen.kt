package com.comye1.wewon

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.comye1.wewon.network.Repository
import com.comye1.wewon.network.WeWonApi
import com.comye1.wewon.network.models.WordResponse
import com.comye1.wewon.ui.theme.Cream
import com.comye1.wewon.ui.theme.PointBlue

@Composable
fun QuizScreen(navController: NavHostController, repository: Repository) {
    var words by rememberSaveable {
        mutableStateOf(listOf<WordResponse>())
    }
    LaunchedEffect(true) {
        words = WeWonApi.retrofitService.getWordScrap(
            username = repository.getSavedUser().username,
            header = repository.getHeaderMap()
        ).shuffled()
    }

    val (position, setPosition) = remember {
        mutableStateOf(0)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "단어를 맞춰보세요.")
                },

                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }) {
        if (words.isNotEmpty()) {
            QuizContent(
                wordResponse = words[position],
                last = (position + 1 == words.size),
                next = { setPosition(position + 1) },
            )
        }
    }

}

@Composable
fun QuizContent(
    wordResponse: WordResponse,
    last: Boolean,
    next: () -> Unit
) {

    val wholeString = wordResponse.sentence
    val word = wordResponse.word
    val replaceWord = "?".repeat(word.length)
    val replaced = wholeString.replace(word, replaceWord)

    var finished by remember {
        mutableStateOf(false)
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(.4f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = replaced, style = MaterialTheme.typography.h5)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top
        ) {
            AnswerField(word, { if (!last) next() }, last) { finished = true }
            Spacer(modifier = Modifier.height(16.dp))
            if (finished) {
                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "모든 퀴즈를 다 풀었습니다!",
                        style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Center),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .clip(CircleShape)
                            .background(Cream)
                            .padding(8.dp),

                        )
                }
            }

        }

    }
}

@Composable
fun AnswerField(word: String, onNext: () -> Unit, last: Boolean, finished: () -> Unit) {

    val context = LocalContext.current

    var text by remember { mutableStateOf("") }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            Modifier
                .fillMaxWidth(.75f)
                .clip(CircleShape)
                .background(Cream),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = text,
                placeholder = {
                    Text(
                        text = "정답 입력",
                        style = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                    )
                },
                onValueChange = { if (it.length <= word.length) text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .padding(4.dp),
                textStyle = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Center),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = PointBlue,
                    backgroundColor = Transparent,
                    focusedBorderColor = Transparent,
                    unfocusedBorderColor = Transparent
                ),

                )
        }

        Box(modifier = Modifier.width(16.dp)) {

        }

        Button(modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape), onClick = {
            if (word == text) {
                Toast.makeText(context, "정답입니다.", Toast.LENGTH_SHORT).show()
                onNext()
                text = ""
                if (last) {
                    finished()
                }
            } else {
                Toast.makeText(context, "오답입니다.", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "확인", style = MaterialTheme.typography.h6)
        }
    }
}