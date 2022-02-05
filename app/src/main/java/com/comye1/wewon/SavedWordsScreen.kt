package com.comye1.wewon;

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.comye1.wewon.network.Repository
import com.comye1.wewon.network.WeWonApi
import com.comye1.wewon.network.models.WordResponse
import com.comye1.wewon.ui.theme.Beige
import com.comye1.wewon.ui.theme.Cream
import com.comye1.wewon.ui.theme.PointBlue
import com.comye1.wewon.util.simpleVerticalScrollbar

@Composable
fun SavedWordsScreen(navController: NavHostController, repository: Repository) {
    var words by rememberSaveable {
        mutableStateOf(listOf<WordResponse>())
    }
    LaunchedEffect(true) {
        words = WeWonApi.retrofitService.getWordScrap(
            username = repository.getSavedUser().username,
            header = repository.getHeaderMap()
        )
    }
//    WritingsContent(navController, writings)

    WordsContent(words)
}

@Composable
fun WordsContent(words: List<WordResponse>) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .simpleVerticalScrollbar(lazyListState)
    ) {
        item {
            WordHeader()
        }
        items(words) {
            WordItem(it)
        }
    }
}

@Composable
fun WordHeader() {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(
            Modifier
                .fillMaxSize(.4f)
                .padding(horizontal = 4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Beige)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "단어", style = MaterialTheme.typography.h6)
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Beige)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "문장", style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
fun WordItem(word: WordResponse) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(
            Modifier
                .fillMaxWidth(.4f)
                .padding(4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Cream)
                .padding(vertical = 4.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = word.word,
                style = MaterialTheme.typography.h5,
                color = PointBlue,
            )
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = word.sentence, style = MaterialTheme.typography.h6)
        }
    }
}