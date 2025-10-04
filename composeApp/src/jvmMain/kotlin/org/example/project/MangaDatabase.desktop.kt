package org.example.project

import androidx.room.Room
import androidx.room.RoomDatabase
import org.example.project.database.MangaDatabase
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<MangaDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "my_room.db")
    return Room.databaseBuilder<MangaDatabase>(
        name = dbFile.absolutePath,
    )
}
