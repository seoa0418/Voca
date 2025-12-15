package com.example.voca

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// WordUiState는 기존과 동일합니다.
data class WordUiState(
    val english: String = "...",
    val korean: String = "...",
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class WordViewModel : ViewModel() {

    var uiState by mutableStateOf(WordUiState())
        private set

    private val commonWords = listOf(
        "ability", "able", "about", "above", "accept", "according", "account", "across", "act", "action",
        "activity", "actually", "add", "address", "administration", "admit", "adult", "affect", "after",
        "again", "against", "age", "agency", "agent", "ago", "agree", "agreement", "ahead", "air",
        "all", "allow", "almost", "alone", "along", "already", "also", "although", "always", "American"
        // ...
    )

    init {
        fetchNextWord()
    }

    fun fetchNextWord() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                // 1. 랜덤 영어 단어 선택
                val randomWord = commonWords.random()

                // (선택적) Dictionary API로 단어가 실제로 있는지 확인하고, 간단한 뜻 가져오기
                // 이 부분은 생략하고 바로 번역 API를 호출해도 됩니다.
                val wordInfoList = ApiClient.dictionaryService.getWordInfo(randomWord)
                val englishDefinition = wordInfoList.firstOrNull()
                    ?.meanings?.firstOrNull()
                    ?.definitions?.firstOrNull()
                    ?.definition

                // 번역 API를 호출하여 한글 뜻 가져오기
                // 여기서는 간단하게 단어 자체(randomWord)를 번역합니다.
                val translationResponse = ApiClient.translationService.translate(randomWord)
                val koreanMeaning = translationResponse.responseData.translatedText

                // 4. 상태 업데이트
                uiState = uiState.copy(
                    english = randomWord,
                    korean = koreanMeaning, // 한글 뜻으로 업데이트
                    isLoading = false
                )

            } catch (e: Exception) {
                // API 호출 실패 시 에러 처리
                uiState = uiState.copy(isLoading = false, errorMessage = "단어를 불러오는 데 실패했습니다: ${e.message}")
            }
        }
    }
}