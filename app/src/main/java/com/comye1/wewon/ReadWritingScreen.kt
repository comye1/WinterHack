package com.comye1.wewon

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.comye1.wewon.navigation.Screen
import com.comye1.wewon.network.Repository
import com.comye1.wewon.network.WeWonApi
import com.comye1.wewon.network.models.ScrapBody
import com.comye1.wewon.network.models.Sentence
import com.comye1.wewon.network.models.WordBody
import com.comye1.wewon.network.models.Writing
import com.comye1.wewon.ui.theme.Cream
import com.comye1.wewon.ui.theme.PointBlue
import com.comye1.wewon.util.checkAudioPermission
import com.comye1.wewon.util.similar
import com.comye1.wewon.util.simpleVerticalScrollbar
import com.comye1.wewon.util.startSpeechToText
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import kotlin.math.max
import kotlin.math.min

@Composable
fun ReadWritingScreen(
    navController: NavHostController,
    random: Boolean,
    wrt: Writing?,
    repository: Repository
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var writing by remember {
        mutableStateOf(
            Writing(
                id = -1, title = "", "", "", "", 1
            )
        )
    }
    if (wrt != null) {
        writing = wrt
    }
    if (random) {
        LaunchedEffect(true) {
            writing = WeWonApi.retrofitService.getRandomWriting(repository.getHeaderMap())[0]
        }
    }
    val (bookmarked, bookmark) = remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = writing.title)
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            val body = ScrapBody(repository.getSavedUser().username, writing.title)
                            val header = repository.getHeaderMap()
                            if (WeWonApi.retrofitService.scrapWriting(body, header).success) {
                                Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_LONG).show()
                                bookmark(!bookmarked)
                            }
                        }
                    }) {
                        if (bookmarked) {
                            Icon(
                                imageVector = Icons.Default.BookmarkAdded,
                                contentDescription = "글 저장하기"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.BookmarkAdd,
                                contentDescription = "글 저장하기"
                            )

                        }
                    }
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
        }
    )
    {
//        CompositionLocalProvider(
//            LocalTextToolbar provides CustomTextToolbar(LocalView.current)
//        ) {
        ReadWritingContent(writing, navController, repository)
//        }
    }
}

@Composable
fun ReadWritingContent(
    writing: Writing,
    navController: NavHostController,
    repository: Repository
) {

    val (selectedSentence, setSentence) = remember {
        mutableStateOf("문장을 선택하세요.")
    }

    val (selectedId, setSentenceId) = remember {
        mutableStateOf(-1)
    }

    val context = LocalContext.current

    val lazyListState = rememberLazyListState()

    val scope = rememberCoroutineScope()

    val uriHandler = LocalUriHandler.current

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxHeight(.6f)
        ) {
            Text(text = "글쓴이 - ${writing.writer}")

            if (writing.site_url != "null") {
                writing.site_url?.let { // 출처 사이트 연결
                    val decodedURL = URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    ClickableText(
                        text = buildAnnotatedString {
                            append(it)
                            addStyle(
                                style = SpanStyle(
                                    color = PointBlue,
                                    textDecoration = TextDecoration.Underline
                                ),
                                start = 0,
                                end = it.length
                            )
                            addStringAnnotation(
                                tag = "URL",
                                annotation = decodedURL,
                                start = 0,
                                end = it.length
                            )
                        },
                        onClick = {
                            uriHandler.openUri(decodedURL)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.simpleVerticalScrollbar(lazyListState)
            ) {
                writing.content.split(".").forEachIndexed { index, s ->
                    if (s.isNotBlank()) {
                        item {
                            SelectableSentence(
                                sentence = "$s.",
                            ) {
                                setSentence("$s.")
                                setSentenceId(writing.id * 1000 + index) // SelectableSentence에서 id를 설정
                            }
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(.4f),
            verticalArrangement = Arrangement.Bottom
        ) {
            Divider(Modifier.height(8.dp))
            SelectedSentence(
                sentence = selectedSentence,
                {
                    if (selectedId != -1) {
                        scope.launch {
                            val response = WeWonApi.retrofitService.scrapSentence(
                                repository.getHeaderMap(),
                                Sentence(
                                    sentence = selectedSentence,
                                    title = writing.title,
                                    username = repository.getSavedUser().username,
                                    sentenceId = selectedId
                                )
                            )
                            if (response.success) {
                                Toast.makeText(context, "문장이 저장되었습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        // 문장 저장하기 selectedId, username
                    }
                },
                navController = navController,
                repository = repository
            )
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
        backgroundColor = Cream,
    ) {
        Box(Modifier.padding(8.dp)) {
            Text(text = sentence, style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
fun SelectedSentence(
    sentence: String,
    saveSentence: () -> Unit,
    navController: NavHostController,
    repository: Repository
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val (STTActivated, STTActivate) = remember {
        mutableStateOf(false)
    }

    val (recognizedText, setRecognizedText) = remember {
        mutableStateOf("")
    }

    val (selectedWord, selectWord) = remember {
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
            .fillMaxHeight()
//            .defaultMinSize(minHeight = 200.dp)
            .padding(16.dp)
    ) {
        LazyColumn(Modifier.fillMaxHeight(.7f)) {
            item {
                AndroidView(factory = { context ->
                    TextView(context).apply {
                        setTextColor(-16777216)
                        setTextIsSelectable(true)
                        textSize = 18f
                        customSelectionActionModeCallback = object : ActionMode.Callback {
                            // Called when the action mode is created; startActionMode() was called
                            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                                // Inflate a menu resource providing context menu items
                                menu.add(1, 123, 0, "단어와 문장 저장하기")
                                    .setIcon(R.drawable.outline_search_24)
                                return true
                            }

                            // Called each time the action mode is shown. Always called after onCreateActionMode, but
                            // may be called multiple times if the mode is invalidated.
                            override fun onPrepareActionMode(
                                mode: ActionMode,
                                menu: Menu
                            ): Boolean {
                                return false // Return false if nothing is done
                            }

                            // Called when the user selects a contextual menu item
                            override fun onActionItemClicked(
                                mode: ActionMode,
                                item: MenuItem
                            ): Boolean {
                                return when (item.itemId) {
                                    123 -> {
                                        var min = 0
                                        var max = text.length
                                        if (isFocused) {
                                            val selStart = selectionStart
                                            val selEnd = selectionEnd

                                            min = max(0, min(selStart, selEnd))
                                            max = max(0, max(selStart, selEnd))
                                        }
                                        selectWord(text.substring(min, max))
                                        mode.finish() // Action picked, so close the CAB
                                        true
                                    }
                                    else -> false
                                }
                            }

                            override fun onDestroyActionMode(p0: ActionMode?) {

                            }

                        }
                    }
                }, update = { view ->
                    view.text = sentence
                })
            }
            item {
                Text(text = recognizedText, style = MaterialTheme.typography.h6, color = PointBlue)
            }
            item {
                if (recognizedText.isNotBlank()) {
                    Text(text = sentence.similar(recognizedText).toString())
                }
            }
        }

        val lazyListState = rememberLazyListState()

        if (selectedWord.isNotBlank()) {
            Dialog(onDismissRequest = { selectWord("") }) {
                Column(
                    Modifier
                        .fillMaxHeight(.6f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    LazyColumn(modifier = Modifier.simpleVerticalScrollbar(lazyListState)) {
                        item {
                            Text(text = "문장 : $sentence", style = MaterialTheme.typography.h6)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "단어 : $selectedWord", style = MaterialTheme.typography.h6)
                        }

                    }

                    Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                // 저장하기
                                scope.launch {
                                    val result = WeWonApi.retrofitService.scrapWord(
                                        repository.getHeaderMap(),
                                        WordBody(
                                            username = repository.getSavedUser().username,
                                            sentence = sentence,
                                            word = selectedWord
                                        )
                                    )
                                    if (result.success) {
                                        Toast.makeText(context, "단어 및 문장 저장 성공", Toast.LENGTH_SHORT).show()
                                    }else{

                                        Toast.makeText(context, "단어 및 문장 저장 실패", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                selectWord("")
                            })
                        {
                            Text(text = "저장하기")
                        }
                    }
                }
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .height(80.dp)
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(onClick = { saveSentence() }) {
                Text(text = "문장 저장하기")
            }
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .clickable {
                        if (sentence == "문장을 선택하세요.") {
                            Toast
                                .makeText(context, "문장을 먼저 선택해주세요.", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            checkAudioPermission(context)
                            startSpeechToText(context, listener)
                        }
                    }
                    .background(if (STTActivated) PointBlue else Cream),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    tint = if (STTActivated) Cream else PointBlue,
                    modifier = Modifier.size(64.dp),
                    contentDescription = "마이크"
                )
            }
            Button(
                onClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                enabled = (sentence.similar(recognizedText) > 0.85)
            ) {
                Text(text = "완료")
            }
            Button(
                onClick = {
                    navController.popBackStack()
                    navController.navigate(Screen.Random.route)
                },
            ) {
                Text(text = "다음")
            }
        }
    }
}
