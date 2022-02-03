package com.comye1.wewon.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import kotlin.math.min


fun String.similar(str: String): Float {
    val len = min(this.length, str.length)
    val distance = levenshtein(this, str)
    return 1 - distance.toFloat() / len
}

fun levenshtein(lhs : CharSequence, rhs : CharSequence) : Int {
    val lhsLength = lhs.length
    val rhsLength = rhs.length

    var cost = Array(lhsLength) { it }
    var newCost = Array(lhsLength) { 0 }

    for (i in 1..rhsLength-1) {
        newCost[0] = i

        for (j in 1..lhsLength-1) {
            val match = if(lhs[j - 1] == rhs[i - 1]) 0 else 1

            val costReplace = cost[j - 1] + match
            val costInsert = cost[j] + 1
            val costDelete = newCost[j - 1] + 1

            newCost[j] = Math.min(Math.min(costInsert, costDelete), costReplace)
        }

        val swap = cost
        cost = newCost
        newCost = swap
    }

    return cost[lhsLength - 1]
}

fun checkAudioPermission(context: Context) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // M = 23
        var permission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
        if(permission != PackageManager.PERMISSION_GRANTED) {
            // this will open settings which asks for permission
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.comye1.wewon"))
            startActivity(context, intent, null)
            Toast.makeText(context, "마이크 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}

fun startSpeechToText(context: Context, listener: RecognitionListener) {
    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    speechRecognizerIntent.putExtra(
        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
    )
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")

    speechRecognizer.setRecognitionListener(listener)
    // starts listening ...
    speechRecognizer.startListening(speechRecognizerIntent)
}