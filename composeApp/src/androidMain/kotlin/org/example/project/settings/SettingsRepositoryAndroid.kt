package org.example.project.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class SettingsRepositoryAndroid(context: Context) : SettingsRepository {
    private val dataStore = context.dataStore
    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

    override val isDarkMode: Flow<Boolean> =
        dataStore.data.map { it[DARK_MODE_KEY] ?: false }

    override suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { it[DARK_MODE_KEY] = enabled }
    }
}