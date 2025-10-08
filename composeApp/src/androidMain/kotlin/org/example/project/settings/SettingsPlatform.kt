package org.example.project.settings

import android.content.Context

lateinit var appContext: Context

fun initSettings(context: Context) {
    appContext = context.applicationContext
}

actual fun getSettingsRepository(): SettingsRepository =
    SettingsRepositoryAndroid(appContext)