package com.example.greetingcard

import android.app.DownloadManager.Request
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.TableInfo
import com.example.greetingcard.database.AppDatabase
import com.example.greetingcard.database.MangaRepository
import com.example.greetingcard.database.MangaViewModel
import com.example.greetingcard.database.MangaViewModelFactory
import com.example.greetingcard.pages.MySettingsScreen
import com.example.greetingcard.settings.SettingsViewModel
import com.example.greetingcard.settings.SettingsViewModelFactory
import com.example.greetingcard.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.Call
import okhttp3.OkHttpClient
import java.io.IOException


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)

    private lateinit var mangaViewModel: MangaViewModel
    private val settingsViewModel: SettingsViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(applicationContext)
            )
            val isDarkMode = settingsViewModel.isDarkMode.collectAsState()

            AppTheme(darkTheme = isDarkMode.value) {

                val mangaDao = AppDatabase.getDatabase(application).mangaDao()
                val mangaRepository = MangaRepository(mangaDao)
                val mangaViewModel: MangaViewModel = viewModel(
                    factory = MangaViewModelFactory(mangaRepository)
                )


                // Wrap in a single composable:
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Compose your screens inside here — conditionally or via Navigation
                    MainScreen(mangaViewModel = mangaViewModel)
                    // If using settings screen as a separate screen, navigate to it properly
                    // MySettingsScreen(viewModel = settingsViewModel)
                }
            }
        }
    }
    }

