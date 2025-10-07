package org.example.project.di

import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.database.MangaDao
import org.example.project.database.MangaDatabase
import org.example.project.database.MangaRepository
import org.example.project.database.MangaViewModel
import org.example.project.database.getRoomDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val androidModule = module {
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
}