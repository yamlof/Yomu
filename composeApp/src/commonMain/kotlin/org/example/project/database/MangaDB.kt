package org.example.project.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MangaEntity::class], version = 1
)
abstract class MangaDB : RoomDatabase() {
    abstract val MangaDao : MangaDao

    companion object{
        const val DATABASE_NAME ="mangas.db"
    }
}