package org.example.project.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "mangas", indices = [Index(value = ["mangaUrl"], unique = true)])
data class MangaEntity(
    @PrimaryKey val name : String,
    val cover : String,
    val mangaUrl : String,
    val source : String
)

