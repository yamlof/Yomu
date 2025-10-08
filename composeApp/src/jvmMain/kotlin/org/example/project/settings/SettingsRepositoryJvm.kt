package org.example.project.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.prefs.Preferences

class SettingsRepositoryJvm : SettingsRepository {
    private val prefs = Preferences.userRoot().node("settings")
    private val key = "dark_mode"

    private val _isDarkMode = MutableStateFlow(prefs.getBoolean(key, false))
    override val isDarkMode: Flow<Boolean> = _isDarkMode

    override suspend fun setDarkMode(enabled: Boolean) {
        prefs.putBoolean(key, enabled)
        _isDarkMode.value = enabled
    }
}