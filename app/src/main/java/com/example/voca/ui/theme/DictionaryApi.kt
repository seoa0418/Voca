package com.example.voca

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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

data class TranslationResponse(
    @Json(name = "responseData") val responseData: ResponseData
)

data class ResponseData(
    @Json(name = "translatedText") val translatedText: String
)



private const val DICT_BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"

interface DictionaryApiService {
    @GET("{word}")
    suspend fun getWordInfo(@Path("word") word: String): List<WordInfo>
}

private const val TRANSLATE_BASE_URL = "https://api.mymemory.translated.net/"

interface TranslationApiService {
    @GET("get")
    suspend fun translate(
        @Query("q") textToTranslate: String,
        @Query("langpair") languagePair: String = "en|ko"
    ): TranslationResponse
}



private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val dictionaryRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(DICT_BASE_URL)
    .build()

private val translationRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(TRANSLATE_BASE_URL)
    .build()


object ApiClient {
    val dictionaryService: DictionaryApiService by lazy {
        dictionaryRetrofit.create(DictionaryApiService::class.java)
    }

    val translationService: TranslationApiService by lazy {
        translationRetrofit.create(TranslationApiService::class.java)
    }
}
