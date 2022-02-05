package com.comye1.wewon

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.comye1.wewon.navigation.Screen
import com.comye1.wewon.network.Repository
import com.comye1.wewon.network.WeWonApi
import com.comye1.wewon.network.models.Sentence
import com.comye1.wewon.network.models.SentenceResponse
import com.comye1.wewon.network.models.Writing
import com.comye1.wewon.ui.theme.Cream
import com.comye1.wewon.util.simpleVerticalScrollbar
import kotlinx.coroutines.launch
import java.lang.Exception

@Composable
fun SavedSentencesScreen(navController: NavHostController, repository: Repository) {
    var sentences by rememberSaveable {
        mutableStateOf(listOf<SentenceResponse>())
    }
    LaunchedEffect(true) {
        sentences = WeWonApi.retrofitService.getSentenceScraps(
            username = repository.getSavedUser().username,
            header = repository.getHeaderMap()
        )
    }
    SentencesContent(sentences, navController, repository)
}

@Composable
fun SentencesContent(
    sentences: List<SentenceResponse>,
    navController: NavHostController,
    repository: Repository
) {
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .simpleVerticalScrollbar(lazyListState)
    ) {
        items(sentences) { item ->
            SentenceItem(
                sentence = item.sentence,
                title = item.title
            ) {
                scope.launch {
                    val writing = WeWonApi.retrofitService.getWriting(item.title, repository.getHeaderMap())
                    navController.navigate(Screen.Read.route + "/${writing.id}/${writing.title}/${writing.writer}/${writing.site_url}/${writing.content}/${writing.category}")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SentenceItem(sentence: String, title: String, onClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable {
                onClick()
            }
            .background(Cream)
            .padding(8.dp)
    ) {
        Icon(imageVector = Icons.Default.FormatQuote, contentDescription = "quotes icon")
        Text(text = sentence, style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(4.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Text(text = "『$title』", style = MaterialTheme.typography.subtitle1)

        }
    }
}
