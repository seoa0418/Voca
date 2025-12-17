package com.example.voca

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

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
                val randomWord = commonWords.random()

                val wordInfoList = ApiClient.dictionaryService.getWordInfo(randomWord)
                val englishDefinition = wordInfoList.firstOrNull()
                    ?.meanings?.firstOrNull()
                    ?.definitions?.firstOrNull()
                    ?.definition

                val translationResponse = ApiClient.translationService.translate(randomWord)
                val koreanMeaning = translationResponse.responseData.translatedText

                uiState = uiState.copy(
                    english = randomWord,
                    korean = koreanMeaning,
                    isLoading = false
                )

            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, errorMessage = "단어를 불러오는 데 실패했습니다: ${e.message}")
            }
        }
    }
}
