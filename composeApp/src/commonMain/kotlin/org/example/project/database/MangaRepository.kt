package org.example.project.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.example.project.database.MangaDao

class MangaRepository (
    private val mangaDao: MangaDao
){

    val allMangas: StateFlow<List<MangaEntity>> =
        mangaDao.getAllMangas()
            .stateIn(
                scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )

    suspend fun insertManga(manga: MangaEntity) {
        mangaDao.insertManga(manga)
    }

    suspend fun updateManga(manga: MangaEntity){
        mangaDao.update(manga)
    }

    suspend fun  deleteManga(manga: String){
        mangaDao.delete(manga)
    }

    fun getAllMangas() : Flow<List<MangaEntity>> {
        return mangaDao.getAllMangas()
    }

}