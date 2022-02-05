package com.comye1.wewon

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import com.comye1.wewon.network.Repository
import com.comye1.wewon.network.WeWonApi
import com.comye1.wewon.network.models.CATEGORY_ESSAY
import com.comye1.wewon.network.models.CATEGORY_NOVEL
import com.comye1.wewon.network.models.CATEGORY_POEM
import com.comye1.wewon.network.models.Writing

@Composable
fun CategoryReading(category: Int = -1, navController: NavHostController, repository: Repository) {
    var writings by rememberSaveable {
        mutableStateOf(listOf<Writing>())
    }

    val header = repository.getHeaderMap()

    when (category) {
        CATEGORY_ESSAY -> {
            LaunchedEffect(true) {
                writings = WeWonApi.retrofitService.getEssays(header = header)
            }
        }
        CATEGORY_NOVEL -> {
            LaunchedEffect(true) {
                writings = WeWonApi.retrofitService.getNovels(header = header)
            }
        }
        CATEGORY_POEM -> {
            LaunchedEffect(true) {
                writings = WeWonApi.retrofitService.getPoems(header = header)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (category) {
                            1 -> "시 읽기"
                            2 -> "소설 읽기"
                            3 -> "수필 읽기"
                            else -> "오류 발생"
                        }
                    )
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

        WritingsContent(navController, writings)
    }

}