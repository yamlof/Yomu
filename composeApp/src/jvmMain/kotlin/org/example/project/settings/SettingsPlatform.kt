package org.example.project.settings


actual fun getSettingsRepository(): SettingsRepository =
    SettingsRepositoryJvm()