package org.example.project.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private const val baseUrl = "https://manga-sp-faecffgcasbjgdcp.switzerlandnorth-01.azurewebsites.net/manga/"
    private const val testUrl = "http://10.24.72.29:5000/manga/"

    suspend fun getHelloMessage(): String =
        client.get("${baseUrl}hello").body()

    suspend fun getLatest(): List<LatestManga> =
        client.get("${testUrl}latest").body()


    suspend fun getPopular(): List<LatestManga> =
        client.get("${baseUrl}popular").body()

    suspend fun getMangaInfo(url: String): MangaInfo =
        client.get("${testUrl}manga_info") {
            parameter("mangaInfo", url)
        }.body()

    suspend fun getChapterInfo(url: String): List<ImageManga> =
        client.get("${testUrl}chapter") {
            parameter("chapterUrl", url)
        }.body()

    suspend fun getSearchInfo(query: String): List<LatestManga> =
        client.get("${baseUrl}search") {
            parameter("mangaString", query)
        }.body()

}