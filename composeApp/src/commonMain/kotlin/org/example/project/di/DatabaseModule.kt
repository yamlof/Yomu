package org.example.project.di

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import org.example.project.database.MangaDao
import org.example.project.database.MangaDatabase
import org.example.project.database.MangaRepository
import org.example.project.database.MangaViewModel
import org.example.project.database.getRoomDatabase
import org.example.project.settings.SettingsViewModel
import org.example.project.source.SearchView
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module


val commonModule = module {

    single<MangaDatabase> {
        getRoomDatabase(
            builder = get()
        )
    }

    single<MangaDao> {
        get<MangaDatabase>().getDao()
    }

    singleOf(::MangaRepository)
    viewModelOf(::MangaViewModel)
    viewModelOf(::SearchView)
    singleOf(::SettingsViewModel)
    //viewModelOf(::SettingsViewModel)
}