package org.example.project.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaDao {
    @Insert
    suspend fun insertManga(manga: MangaEntity)

    @Update
    suspend fun update(manga: MangaEntity)

    @Query(value = "DELETE FROM mangas WHERE name = :name")
    suspend fun delete(name: String)

    @Query(value = "SELECT * FROM mangas")
    fun getAllMangas() : Flow<List<MangaEntity>>
}