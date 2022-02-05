package com.comye1.wewon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.comye1.wewon.network.Repository
import com.comye1.wewon.network.WeWonApi
import com.comye1.wewon.network.models.VocaItem
import com.comye1.wewon.ui.theme.Cream
import com.comye1.wewon.ui.theme.PointBlue
import com.comye1.wewon.util.simpleVerticalScrollbar

@Composable
fun VocaScreen(navController: NavHostController, repository: Repository) {

    var words by rememberSaveable {
        mutableStateOf(listOf<VocaItem>())
    }

    val header = repository.getHeaderMap()

    LaunchedEffect(true) {
        words = WeWonApi.retrofitService.getVoca(header)
    }

    VocaContent(words)
}

@Composable
fun VocaContent(words: List<VocaItem>) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .simpleVerticalScrollbar(lazyListState)
    ) {
        items(words) {
            VocaItem(it)
        }
    }
}

@Composable
fun VocaItem(vocaItem: VocaItem) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(
            Modifier
                .fillMaxWidth(.4f)
                .padding(4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Cream)
                .padding(vertical = 4.dp, horizontal = 16.dp)
        ) {
            Text(
                text = vocaItem.voca,
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
            Text(text = vocaItem.meaning, style = MaterialTheme.typography.h6)
        }
    }
}
