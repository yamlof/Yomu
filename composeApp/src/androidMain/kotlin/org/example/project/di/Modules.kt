package org.example.project.di

import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.database.MangaViewModel
import org.koin.dsl.module


val androidModule = module {
    viewModel { MangaViewModel(get()) } // Koin Android ViewModel
}