package com.example.voca

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Dictionary API 관련 데이터 클래스
data class WordInfo(
    val word: String,
    val meanings: List<Meaning>
)

data class Meaning(
    val partOfSpeech: String,
    val definitions: List<Definition>
)

data class Definition(
    val definition: String
)

// 번역 API(MyMemory) 관련 데이터 클래스
data class TranslationResponse(
    @Json(name = "responseData") val responseData: ResponseData
)

data class ResponseData(
    @Json(name = "translatedText") val translatedText: String
)


// Retrofit API 인터페이스 정의

// Dictionary API
private const val DICT_BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"

interface DictionaryApiService {
    @GET("{word}")
    suspend fun getWordInfo(@Path("word") word: String): List<WordInfo>
}

// 번역 API
private const val TRANSLATE_BASE_URL = "https://api.mymemory.translated.net/"

interface TranslationApiService {
    // GET 요청 예시: /get?q=hello&langpair=en|ko
    @GET("get")
    suspend fun translate(
        @Query("q") textToTranslate: String,
        @Query("langpair") languagePair: String = "en|ko" // 영어(en)를 한국어(ko)로
    ): TranslationResponse
}


// Retrofit 객체 생성

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Dictionary API용 Retrofit 인스턴스
private val dictionaryRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(DICT_BASE_URL)
    .build()

// 번역 API용 Retrofit 인스턴스
private val translationRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(TRANSLATE_BASE_URL)
    .build()


// 외부에서 사용할 서비스 객체
object ApiClient {
    val dictionaryService: DictionaryApiService by lazy {
        dictionaryRetrofit.create(DictionaryApiService::class.java)
    }

    // 번역 서비스 객체
    val translationService: TranslationApiService by lazy {
        translationRetrofit.create(TranslationApiService::class.java)
    }
}