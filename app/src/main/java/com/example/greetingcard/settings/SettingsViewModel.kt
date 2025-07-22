package com.example.greetingcard.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dataStore

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class SettingsViewModel(context: Context) : ViewModel() {



    private val dataStore = context.dataStore

    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.data
                .map { preferences -> preferences[DARK_MODE_KEY] ?: false }
                .collect { _isDarkMode.value = it }
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[DARK_MODE_KEY] = enabled
            }
        }
    }
}