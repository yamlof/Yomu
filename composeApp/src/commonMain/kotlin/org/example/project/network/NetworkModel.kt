package org.example.project.network

import kotlinx.serialization.Serializable

@Serializable
data class LatestManga(
    val cover:String,
    val title:String,
    val manga_url : String
)


@Serializable
data class MangaInfo(
    val title : String,
    val cover : String,
    val author : String,
    val status : String,
    val chapters : List<Chapter>
)

@Serializable
data class Chapter(
    val id : Int,
    val number : String? ,
    val title:String,
    val url:String,
)

@Serializable
data class ImageManga (
    val imgLink:String,
    val imgTitle:String,

    )