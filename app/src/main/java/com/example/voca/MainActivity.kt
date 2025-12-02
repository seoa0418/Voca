package com.example.voca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.voca.ui.theme.VocaTheme

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VocaTheme {
                // 앱의 메인 화면 Composable 함수 호출
                VocabularyScreen()
            }
        }
    }
}

/**
 * 💡 핵심 로직: 단어 학습 화면
 * 상태 관리를 통해 현재 단어, 인덱스, 뜻 표시 여부를 제어합니다.
 */


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun VocabularyScreen() {
    // 현재 단어의 인덱스를 저장하는 상태 변수. remember를 사용하여 Composable이 리컴포지션 되어도 값이 유지됨.
    var currentIndex by remember { mutableStateOf(0) }
    // 현재 단어의 뜻이 표시되어야 하는지 여부를 저장하는 상태 변수
    var showMeaning by remember { mutableStateOf(false) }

    // 단어 목록의 크기
    val totalWords = wordList.size

    // 현재 표시할 단어
    val currentWord = wordList[currentIndex]

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Voca - 영단어 학습") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // 내용물 사이의 공간을 균등하게 분배
        ) {
            // 1. 단어 및 뜻 영역
            WordDisplay(
                word = currentWord,
                showMeaning = showMeaning,
                modifier = Modifier.weight(1f) // 남은 공간을 최대한 차지
            )

            // 2. 버튼 영역
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround // 버튼 사이에 공간을 균등하게 배치
            ) {
                // 뜻 보기/숨기기 버튼
                Button(
                    onClick = { showMeaning = !showMeaning }, // 상태 반전
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (showMeaning) "뜻 숨기기" else "뜻 보기")
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 다음 단어 버튼
                Button(
                    onClick = {
                        // 다음 단어로 이동 (순환)
                        currentIndex = (currentIndex + 1) % totalWords
                        // 단어가 바뀌면 뜻은 다시 숨김
                        showMeaning = false
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("다음 단어")
                }
            }

            // 3. 현재 진행 상황
            Text(
                text = "${currentIndex + 1} / $totalWords",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * 영어 단어와 뜻을 표시하는 Composable
 */
@Composable
fun WordDisplay(word: Word, showMeaning: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 영어 단어 (항상 표시)
        Text(
            text = word.english,
            fontSize = 48.sp,
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 한국어 뜻 (showMeaning 상태에 따라 표시)
        Text(
            text = if (showMeaning) word.korean else "???",
            fontSize = 32.sp,
            color = if (showMeaning) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


/**
 * 🖼️ 프리뷰 구성
 * 이 코드를 통해 Android Studio 디자인 창에서 실행하지 않고도 화면을 미리 볼 수 있습니다.
 */
@Preview(showBackground = true)
@Composable
fun VocabularyScreenPreview() {
    VocaTheme {
        VocabularyScreen()
    }
}