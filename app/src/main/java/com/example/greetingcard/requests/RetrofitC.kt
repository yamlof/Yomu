package com.example.greetingcard.requests

import com.example.greetingcard.models.ImageManga
import com.example.greetingcard.models.LatestManga
import com.example.greetingcard.models.MangaInfo
import com.example.greetingcard.models.Mangadex
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.gson.gson
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.Call

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


object RetrofitClient {
    private val client = OkHttpClient.Builder().build()

    val homeUrl = "http://10.0.2.2:5000/manga/"

    val serverUrl = "https://manga-sp-faecffgcasbjgdcp.switzerlandnorth-01.azurewebsites.net/manga/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(serverUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

interface ApiService {

    @GET("manga")
    suspend fun getManga(): retrofit2.Call<Mangadex>

    @GET("hello")
    suspend fun getHelloMessage(): String

    @GET("latest")
    suspend fun getLatest(): List<LatestManga>

    @GET("popular")
    suspend fun getPopular(): List<LatestManga>

    @GET("manga_info")
    suspend fun  getMangaInfo(
        @retrofit2.http.Query("mangaInfo") url :String
    ) : MangaInfo

    @GET("chapter")
    suspend fun getChapterInfo(
        @retrofit2.http.Query("chapterUrl") url :String
    ) : List<ImageManga>

    @GET("search")
    suspend fun getSearchInfo(
        @retrofit2.http.Query("mangaString") url :String
    ) : List<LatestManga>
}
