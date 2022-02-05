package com.comye1.wewon.network

import com.comye1.wewon.network.models.dictionary.Channel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://opendict.korean.go.kr/ "

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()



interface DictionaryApiService {
    @GET("api/search")
    suspend fun getDefinitions(
        @Query("q") word: String,
        @Query("key") key: String,
        @Query("certkey_no") certkey: Int = 3554
    ): Channel
}
object DictionaryApi {
    val retrofitService: DictionaryApiService by lazy {
        retrofit.create(DictionaryApiService::class.java)
    }
}