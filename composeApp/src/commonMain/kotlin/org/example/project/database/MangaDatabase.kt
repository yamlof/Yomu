package org.example.project.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [MangaEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class MangaDatabase : RoomDatabase() {
    abstract fun getDao(): MangaDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<MangaDatabase> {
    override fun initialize(): MangaDatabase
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<MangaDatabase>
): MangaDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}