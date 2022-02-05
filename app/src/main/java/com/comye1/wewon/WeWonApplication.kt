package com.comye1.wewon

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import com.comye1.wewon.network.Repository

class WeWonApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val ai = applicationContext.packageManager.getApplicationInfo(
            applicationContext.packageName,
            PackageManager.GET_META_DATA
        )
        val value = ai.metaData["keyValue"]
        Repository.initialize(this, value.toString())
        Log.d("cca", "repository initialized")
    }
}