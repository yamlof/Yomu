package org.example.project.source

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import kotlinx.coroutines.launch
import org.example.project.network.ApiClient
import org.example.project.network.LatestManga

class SearchView: ViewModel() {

    val mangas = mutableStateOf<List<LatestManga>>(emptyList())

    private val apiService = ApiClient

    val searchQuery = mutableStateOf("")

    val isLoading = mutableStateOf(false)

    val errorMessage = mutableStateOf<String?>(null)

    fun fetchMangasSearch(query: String) {
        isLoading.value = true
        errorMessage.value = null
        viewModelScope.launch {
            try {
                val newMangas = apiService.getSearchInfo(query)
                mangas.value = newMangas
            } catch (e: Exception) {
                errorMessage.value = "error: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun fetchPopular() {
        isLoading.value = true
        errorMessage.value = null
        viewModelScope.launch {
            try {
                val newMangas = apiService.getPopular()
                mangas.value = newMangas
            } catch (e: Exception) {
                errorMessage.value = "error: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun fetchLatest(){
        isLoading.value = true
        errorMessage.value = null
        viewModelScope.launch {
            try {
                val newMangas = apiService.getLatest()
                mangas.value = newMangas
            } catch (e: Exception) {
                errorMessage.value = "error: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }
}