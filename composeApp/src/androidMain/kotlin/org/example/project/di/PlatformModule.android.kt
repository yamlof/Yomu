package org.example.project.di

import android.content.Context
import androidx.room.Room
import org.example.project.database.MangaDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule = module {
    single {
        // Inject Android Context
        val context: Context = get()

        Room.databaseBuilder(
            context,
            MangaDatabase::class.java,
            "manga_database.db"
        ).build()
    }
}