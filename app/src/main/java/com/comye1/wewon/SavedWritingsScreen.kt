package com.comye1.wewon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.comye1.wewon.navigation.Screen
import com.comye1.wewon.network.Repository
import com.comye1.wewon.network.WeWonApi
import com.comye1.wewon.network.models.Writing
import com.comye1.wewon.ui.theme.Cream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SavedWritingsScreen(navController: NavHostController, repository: Repository) {
    var writings by rememberSaveable {
        mutableStateOf(listOf<Writing>())
    }
    LaunchedEffect(true) {
        writings = WeWonApi.retrofitService.getScraps(
            username = repository.getSavedUser().username,
            header = repository.getHeaderMap()
        )
    }
    WritingsContent(navController, writings)
}

@Composable
fun WritingsContent(navController: NavHostController, list: List<Writing>) {

//    val list = listOf(
//        Writing(
//            "메밀꽃 필 무렵",
//            "이효석",
//            site_url = null,
//            content = "여름 장이란 애시당초에 글러서, " +
//                    "해는 아직 중천에 있건만 장판은 벌써 쓸쓸하고 더운 햇발이 벌여놓은 " +
//                    "전 휘장 밑으로 등줄기를 훅훅 볶는다.마을 사람들은 거지반 돌아간 뒤요, " +
//                    "팔리지 못한 나뭇군패가 길거리에 궁싯거리고들 있으나 " +
//                    "석윳병이나 받고 고깃마리나 사면 족할 이 축들을 바라고 언제까지든지 " +
//                    "버티고 있을 법은 없다. 춥춥스럽게 날아드는 파리떼도 장난군 각다귀들도 " +
//                    "귀치않다. 얽둑배기요 왼손잡이인 드팀전의 허생원은 기어코 동업의 조선달에게 " +
//                    "낚아보았다.",
//            category = CATEGORY_NOVEL
//        ),
//        Writing(
//            "만무방", "김유정", null, content = "산골에, 가을은 무르녹았다. " +
//                    "아름드리 노송은 삑삑히 늘어박혔다. 무거운 송낙을 머리에 쓰고 건들건들. " +
//                    "새새이 끼인 도토리, 벚, 돌배, 갈잎 들은 울긋불긋. 잔디를 적시며 맑은 샘이 쫄쫄거린다. " +
//                    "산토끼 두 놈은 한가로이 마주 앉아 그 물을 할짝거리고. " +
//                    "이따금 정신이 나는 듯 가랑잎은 부수수 하고 떨린다. " +
//                    "산산한 산들바람. 귀여운 들국화는 그 품에 새뜩새뜩 넘논다. " +
//                    "흙내와 함께 향긋한 땅김이 코를 찌른다. 요놈은 싸리버섯, 요놈은 잎 썩은 내," +
//                    " 또 요놈은 송이―--- 아니, 아니, 가시넝쿨 속에 숨은 박하풀 냄새로군.",
//            category = CATEGORY_POEM
//        ),
//        Writing(
//            "그믐달",
//            "나도향",
//            null,
//            content = "나는 그믐날을 몹시 사랑한다. " +
//                    "그믐날은 요염하여 감히 손을 댈 수도 없고, " +
//                    "말을 붙일 수도 없이 깜찍하게 예쁜 계집 같은 달이 동시에 가슴이 저리도 쓰리도록 가련한 달이다.\n" +
//                    "서산 위에 잠깐 나타났다. 숨어버리는 초생달은 세상을 후려 삼키려는 독부(毒婦)가 아니면 철모르는 처녀 같은 달이지마는, 그믐달은 세상의 갖은 풍상을 다 겪고, 나중에는 그 무슨 원한을 품고서 애처롭게 쓰러지는 원부(怨婦)와 같이 애절하고 애절한 맛이 있다.",
//            category = CATEGORY_ESSAY
//        ),
//        Writing(
//            "윤동주",
//            "소년",
//            site_url = null,
//            content = "여기저기서 단풍잎 같은 슬픈 가을이 뚝뚝 떨어진다. 단풍잎 떨어져 나온 자리마다 봄을 마련해 놓고 나뭇가지 우에 하늘이 펼쳐 있다. 가만히 하늘을 들여다보려면 눈썹에 파란 물감이 든다. 두 손으로 따뜻한 볼을 쓸어보면 손바닥에도 파란 물감이 묻어난다. 다시 손바닥을 들여다본다. 손금에는 맑은 강물이 흐르고, 맑은 강물이 흐르고, 강물 속에는 사랑처럼 슬픈 얼굴----아름다운 순이(順伊)의 얼굴이 어린다. 소년은 황홀히 눈을 감아 본다. 그래도 맑은 강물은 흘러 사랑처럼 슬픈 얼굴-----아름다운 순이(順伊)의 얼굴은 어린다.",
//            category = CATEGORY_POEM
//        )
//    )

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        list.forEach {
            item {
                WritingItem(writing = it) {
                    val encodedUrl = if (it.site_url != null) URLEncoder.encode(
                        it.site_url,
                        StandardCharsets.UTF_8.toString()
                    ) else null
                    navController.navigate(
                        Screen.Read.route + "/${it.id}/${it.title}/${it.writer}/$encodedUrl/${it.content}/${it.category}"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun WritingItem(writing: Writing, onClick: () -> Unit) {
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
        Text(text = writing.title, style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = writing.writer, style = MaterialTheme.typography.h6)
    }
}