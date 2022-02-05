package com.comye1.wewon.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.comye1.wewon.network.models.LogInBody
import com.comye1.wewon.network.models.LogInResponse
import kotlin.reflect.full.createInstance

class Repository private constructor(context: Context, private val key: String) {

    companion object {
        private var INSTANCE: Repository? = null

        fun initialize(context: Context, key: String) {
            if (INSTANCE == null) {
                INSTANCE = Repository(context = context, key = key)
            }
        }

        fun get(): Repository {
            return INSTANCE ?: throw IllegalStateException("Repository must be initialized")
        }
    }

//    /*
//    단어를 사전에서 검색
//     */
//    suspend fun getDefinition(word: String) {
//        val please = Please::class.createInstance()
//        please.trustAllHosts()
//        DictionaryApi.retrofitService.getDefinitions(word, key)
//    }
////        val url =
////            URL("https://stdict.korean.go.kr/api/search.do?key=$key&type_search=search&q=$word")
////
////        with(url.openConnection() as HttpURLConnection) {
////            requestMethod = "GET"  // optional default is GET
////
////            Log.d("http", "\nSent 'GET' request to URL : $url; Response Code : $responseCode")
////
////        }
////    }


    /*
    저장된 아이디, 비밀번호로 자동으로 로그인하고 토큰을 새로 받는다.
     */
    suspend fun logInRefresh(): Boolean {
        val user = getSavedUser()
        Log.d("login repository", user.username)
        if (user.username != "") {
            val response = WeWonApi.retrofitService.logIn(getSavedUser())
            if (response.token != "invalid token") {
                saveUser(user)
                saveToken(response)
                return true
            }
            return false
        }
        return false
    }

    fun logOut() {
        deleteUser()
        deleteToken()
    }

//    suspend fun logIn(identification: Identification) {
//
//        WeWonApi.retrofitService.logIn(identification.id, identification.pw).enqueue(
//            object : Callback<LogInResponse> {
//                override fun onResponse(
//                    call: Call<LogInResponse>,
//                    response: Response<LogInResponse>
//                ) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        Log.e(
//                            "String 결과값",
//                            "response.body().toString() : " + response.body().toString()
//                        )
//                    }
//                }
//
//                override fun onFailure(call: Call<LogInResponse>, t: Throwable) {
//                    TODO("Not yet implemented")
//                }
//            }
//        )
//    }
//        var result: LogInResponse = try {
//            WeWonApi.retrofitService.logIn(identification.id, identification.pw)
//        } catch (e: Exception) {
//            LogInResponse(
//                "invalid token", "invalid user"
//            )
//        }
//        if (result.token != "invalid token"){
//            saveUser(identification = identification)
//            saveToken(result)
//        }
//        return result


//    fun signUp(identification: Identification): String {
//        try {
//            WeWonApi.retrofitService.signUp(identification.id, identification.pw)
//        } catch (e: java.lang.Exception) {
//            return "FAILED"
//        }
//        return "SUCCEED"
//    }

    private val userSharedPref: SharedPreferences = context.getSharedPreferences(
        "user",
        Context.MODE_PRIVATE
    )

    private val tokenSharedPref: SharedPreferences = context.getSharedPreferences(
        "token",
        Context.MODE_PRIVATE
    )

    fun saveUser(logInBody: LogInBody) {
        userSharedPref.edit {
            putString("UN", logInBody.username)
            putString("PW", logInBody.password)
            commit()
        }
        Log.d("login shared", logInBody.toString())
    }

    private fun deleteUser() {
        userSharedPref.edit {
            putString("UN", "")
            putString("PW", "")
            commit()
        }
    }

    fun getSavedUser() = LogInBody(
        userSharedPref.getString("UN", "") ?: "",
        userSharedPref.getString("PW", "") ?: ""
    )

    fun saveToken(logInResponse: LogInResponse) {
        tokenSharedPref.edit {
            putString("TOKEN", logInResponse.token)
            putString("USERNAME", logInResponse.username)
            commit()
        }
    }

    private fun deleteToken() {
        tokenSharedPref.edit{
            putString("TOKEN", "")
            putString("USERNAME", "")
            commit()
        }
    }

    fun getHeaderMap() = mapOf(
        pair = Pair(
            "Authorization",
            "Bearer " + tokenSharedPref.getString("TOKEN", null) ?: ""
        )
    )
}