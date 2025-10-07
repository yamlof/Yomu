package org.example.project.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.isSuccess

val httpClient = HttpClient(){
    defaultRequest {
        header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:131.0) Gecko/20100101 Firefox/131.0")
        header("Accept", "image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5")
        header("Accept-Language", "en-GB,en;q=0.5")
        header("Connection", "keep-alive")
        header("Referer", "https://www.mangabats.com/")
        header("Sec-Fetch-Dest", "image")
        header("Sec-Fetch-Mode", "no-cors")
        header("Sec-Fetch-Site", "cross-site")
        header("Priority", "u=5, i")
        header("Pragma", "no-cache")
        header("Cache-Control", "no-cache")
    }
}

suspend fun fetchImageBytes(url: String): ByteArray? {
    return try {
        val response = httpClient.get(url)
        if (response.status.isSuccess()) response.body() else null
    } catch (e: Exception) {
        println("Error loading image: ${e.message}")
        null
    }
}