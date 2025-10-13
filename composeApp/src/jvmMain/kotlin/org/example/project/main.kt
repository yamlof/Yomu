package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.project.di.initKoin
import java.io.File

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Yomu",
    ) {
        App()

    }
}