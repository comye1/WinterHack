package com.comye1.wewon.network

import com.comye1.wewon.network.models.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://52.78.23.24:8080/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WeWonApiService {

    @POST("users")
    suspend fun signUp(
        @Body body: LogInBody
    ): SignUpResponse

    @POST("users/login")
    suspend fun logIn(
        @Body json: LogInBody
    ): LogInResponse

    @GET("literatures/random")
    suspend fun getRandomWriting(
        @HeaderMap header: Map<String, String>
    ): List<Writing>

    @GET("literatures/1")
    suspend fun getPoems(
        @HeaderMap header: Map<String, String>
    ): List<Writing>

    @GET("literatures/2")
    suspend fun getNovels(
        @HeaderMap header: Map<String, String>
    ): List<Writing>

    @GET("literatures/3")
    suspend fun getEssays(
        @HeaderMap header: Map<String, String>
    ): List<Writing>

    @GET("voca")
    suspend fun getVoca(
        @HeaderMap header: Map<String, String>
    ): List<VocaItem>

    @POST("users/scrap/literature")
    suspend fun scrapWriting(
        @Body body: ScrapBody,
        @HeaderMap header: Map<String, String>
    ): ScrapResponse

    @GET("users/scrap/literature/{username}")
    suspend fun getScraps(
        @Path("username") username: String,
        @HeaderMap header: Map<String, String>
    ): List<Writing>

    @GET("sentence/{title}")
    suspend fun getWriting(
        @Path("title") title: String,
        @HeaderMap header: Map<String, String>
    ): Writing

    @GET("users/scrap/sentence/{username}")
    suspend fun getSentenceScraps(
        @Path("username") username: String,
        @HeaderMap header: Map<String, String>
    ): List<SentenceResponse>

    @POST("users/scrap/sentence")
    suspend fun scrapSentence(
        @HeaderMap header: Map<String, String>,
        @Body json: Sentence
    ): ScrapResponse

    @GET("users/scrap/word/{username}")
    suspend fun getWordScrap(
        @Path("username") username: String,
        @HeaderMap header: Map<String, String>
    ): List<WordResponse>

    @POST("users/scrap/word")
    suspend fun scrapWord(
        @HeaderMap header: Map<String, String>,
        @Body json: WordBody
    ): ScrapResponse

}

object WeWonApi {
    val retrofitService: WeWonApiService by lazy {
        retrofit.create(WeWonApiService::class.java)
    }
}