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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.voca.ui.theme.VocaTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VocaTheme {
                // 앱의 메인 화면 Composable 함수 호출
                // viewModel() 함수가 ViewModel의 생명주기를 관리해줍니다.
                VocabularyScreen()
            }
        }
    }
}


@Composable
fun VocabularyScreen(wordViewModel: WordViewModel = viewModel()) {
    // ViewModel로부터 UI 상태를 가져옵니다.
    val uiState = wordViewModel.uiState
    // ViewModel의 함수를 이벤트 핸들러로 전달합니다.
    val fetchNextWord = { wordViewModel.fetchNextWord() }

    // 실제 UI를 그리는 부분은 VocabularyContent에 위임합니다.
    VocabularyContent(
        uiState = uiState,
        onFetchNextWord = fetchNextWord
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyContent(
    uiState: WordUiState,
    onFetchNextWord: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMeaning by remember(uiState.english) { mutableStateOf(false) }

    // Scaffold의 containerColor를 MaterialTheme.colorScheme.background로 설정하여 배경색 적용
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                uiState.isLoading -> { /* 로딩 UI */ }
                uiState.errorMessage != null -> { /* 에러 UI */ }
                else -> {
                    // 단어 표시 영역 (수정)
                    WordDisplay(
                        english = uiState.english,
                        korean = uiState.korean,
                        showMeaning = showMeaning,
                        modifier = Modifier.weight(1f) // 남은 공간을 모두 차지
                    )

                    // 버튼 영역
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        // ✨ 버튼에 테마 색상이 자동으로 적용됩니다.
                        Button(
                            onClick = { showMeaning = !showMeaning },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp) // 버튼 높이 지정
                        ) {
                            Text(if (showMeaning) "뜻 숨기기" else "뜻 보기")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = onFetchNextWord,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Text("다음 단어")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun WordDisplay(
    english: String,
    korean: String,
    showMeaning: Boolean,
    modifier: Modifier = Modifier
) {
    // ✨ Card Composable을 사용하여 UI 구성
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp), // 모서리를 16dp 만큼 둥글게
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // 카드 배경색 (흰색)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // 약간의 그림자 효과
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), // 카드를 꽉 채움
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 영어 단어
            Text(
                text = english,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface // onSurface (검은색)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 한국어 뜻
            Text(
                text = if (showMeaning) korean else "???",
                style = MaterialTheme.typography.headlineMedium,
                // 뜻이 보일 때는 primary(핑크), 안 보일 때는 회색 계열로
                color = if (showMeaning) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }
    }
}


@Preview(showBackground = true, name = "Default Preview")
@Composable
fun VocabularyScreenPreview() {
    VocaTheme {
        // ViewModel 대신 가짜 WordUiState를 만들어 VocabularyContent에 전달
        val fakeUiState = WordUiState(
            english = "Preview",
            korean = "미리보기 단어의 뜻입니다. 이렇게 길어질 수 있습니다.",
            isLoading = false,
            errorMessage = null
        )
        VocabularyContent(
            uiState = fakeUiState,
            onFetchNextWord = {} // 프리뷰에서는 아무 동작 안 함
        )
    }
}


@Preview(showBackground = true, name = "Loading Preview")
@Composable
fun VocabularyLoadingPreview() {
    VocaTheme {
        val loadingState = WordUiState(isLoading = true)
        VocabularyContent(
            uiState = loadingState,
            onFetchNextWord = {}
        )
    }
}