package com.comye1.wewon

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.comye1.wewon.ui.theme.Purple500
import com.comye1.wewon.util.*

@Composable
fun ReadWritingScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "글 제목")
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.BookmarkAdd,
                            contentDescription = "글 저장하기"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    )
    {
        CompositionLocalProvider(
            LocalTextToolbar provides CustomTextToolbar(LocalView.current)
        ) {
            ReadWritingContent()
        }
    }
}

@Composable
fun ReadWritingContent() {
    val writing = Writing(
        author = "김유정",
        source = "yujeong@seoultech.ac.kr",
        type = 1,
        content = "오늘도 또 우리 수탉이 막 쫓기었다. " +
                "내가 점심을 먹고 나무를 하러 갈 양으로 나올 때이었다." +
                " 산으로 올라서려니까 등뒤에서 푸드득 푸드득 하고 닭의 횃소리가 야단이다." +
                " 깜짝 놀라서 고개를 돌려 보니 아니나 다르랴 두 놈이 또 얼리었다."
    )

    val (selectedSentence, setSentence) = remember {
        mutableStateOf("문장을 선택하세요.")
    }

    val lazyListState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxHeight(.6f)
        ) {
            Text(text = "글쓴이 - ${writing.author}")
            Text(text = "출처 - ${writing.source}")
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.simpleVerticalScrollbar(lazyListState)
            ) {
                writing.content.split(".").forEach {
                    if (it.isNotBlank()) {
                        item {
                            SelectableSentence(sentence = "$it.") {
                                setSentence("$it.")
                            }
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minHeight = 200.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Divider(Modifier.height(8.dp))
            SelectedSentence(sentence = selectedSentence)
        }
    }
}

@Composable
fun SelectableSentence(sentence: String, onSelected: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable { onSelected() },
        elevation = 8.dp
    ) {
        Box(Modifier.padding(8.dp)) {
            Text(text = sentence, style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
fun SelectedSentence(sentence: String) {

    val context = LocalContext.current

    val (STTActivated, STTActivate) = remember {
        mutableStateOf(false)
    }

    val (recognizedText, setRecognizedText) = remember {
        mutableStateOf("")
    }

    val listener = object : RecognitionListener {
        override fun onReadyForSpeech(bundle: Bundle?) {
            //
            STTActivate(true)
        }

        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(v: Float) {}
        override fun onBufferReceived(bytes: ByteArray?) {}
        override fun onEndOfSpeech() {
            //
            STTActivate(false)
        }

        override fun onError(i: Int) {}

        override fun onResults(bundle: Bundle) {
            val result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (result != null) {
                //
                setRecognizedText(result[0] + ".")
            }
        }

        override fun onPartialResults(bundle: Bundle) {}
        override fun onEvent(i: Int, bundle: Bundle?) {}
    }

    Column(
        Modifier
//            .defaultMinSize(minHeight = 200.dp)
            .padding(16.dp)
    ) {
        SelectionContainer() {
            Text(text = sentence, style = MaterialTheme.typography.h6)
        }
        Text(text = recognizedText, style = MaterialTheme.typography.h6, color = Purple500)
        if (recognizedText.isNotBlank()) {
            Text(text = sentence.similar(recognizedText).toString())
        }

        Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "문장 저장하기")
                }
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .clickable {
                            checkAudioPermission(context)
                            startSpeechToText(context, listener)
                        }
                        .background(if (STTActivated) Purple500 else Color.White)
                        .border(border = BorderStroke(2.dp, Purple500), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        tint = if (STTActivated) Color.White else Purple500,
                        modifier = Modifier.size(64.dp),
                        contentDescription = "마이크"
                    )
                }
                Button(
                    onClick = { /*TODO*/ },
                    enabled = (sentence.similar(recognizedText) > 0.85)
                ) {
                    Text(text = "완료")
                }
                Button(
                    onClick = { /*TODO*/ },
                    enabled = (sentence.similar(recognizedText) > 0.85)
                ) {
                    Text(text = "다음")
                }
            }
        }
    }
}

data class Writing(
    val author: String,
    val source: String,
    val type: Int,
    val content: String
)