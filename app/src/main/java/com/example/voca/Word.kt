package com.example.voca

// 단어 정보를 저장하는 데이터 클래스
data class Word(
    val english: String, // 영어 단어
    val korean: String // 한국어 뜻
)

// 학습에 사용할 단어 목록 (간단한 예시)
val wordList = listOf(
    Word("apple", "사과"),
    Word("banana", "바나나"),
    Word("cat", "고양이"),
    Word("dog", "개"),
    Word("elephant", "코끼리")
)