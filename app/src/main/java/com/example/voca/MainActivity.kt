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
                VocabularyScreen()
            }
        }
    }
}


@Composable
fun VocabularyScreen(wordViewModel: WordViewModel = viewModel()) {
    val uiState = wordViewModel.uiState
    val fetchNextWord = { wordViewModel.fetchNextWord() }

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
                uiState.isLoading -> { }
                uiState.errorMessage != null -> { }
                else -> {
                    WordDisplay(
                        english = uiState.english,
                        korean = uiState.korean,
                        showMeaning = showMeaning,
                        modifier = Modifier.weight(1f)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(
                            onClick = { showMeaning = !showMeaning },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
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
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = english,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = if (showMeaning) korean else "???",
                style = MaterialTheme.typography.headlineMedium,
                color = if (showMeaning) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }
    }
}


@Preview(showBackground = true, name = "Default Preview")
@Composable
fun VocabularyScreenPreview() {
    VocaTheme {
        val fakeUiState = WordUiState(
            english = "Preview",
            korean = "미리보기 단어의 뜻입니다. 이렇게 길어질 수 있습니다.",
            isLoading = false,
            errorMessage = null
        )
        VocabularyContent(
            uiState = fakeUiState,
            onFetchNextWord = {}
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
