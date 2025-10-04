package org.example.project.di

import androidx.room.Room
import androidx.room.RoomDatabase
import org.example.project.database.MangaDatabase
import org.koin.dsl.module
import java.io.File

actual val platformModule = module {
    single<RoomDatabase.Builder<MangaDatabase>> {
        val dbFile = File(System.getProperty("java.io.tmpdir"), "manga_database.db")
        Room.databaseBuilder<MangaDatabase>(
            name = dbFile.absolutePath,
        )
    }
}