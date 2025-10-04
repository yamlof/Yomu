package org.example.project.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class BaseViewModel {
    protected val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    open fun clear(){
        viewModelScope.cancel()
    }
}

class MangaViewModel (
    private val mangaRepository: MangaRepository
): ViewModel() {

    val allMangas : StateFlow<List<MangaEntity>> = mangaRepository.allMangas

    fun addManga(manga: MangaEntity){
        viewModelScope.launch {
            mangaRepository.insertManga(manga)
        }
    }

}