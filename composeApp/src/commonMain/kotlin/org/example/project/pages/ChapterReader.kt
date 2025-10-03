package org.example.project.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.seiko.imageloader.rememberImagePainter
import org.example.project.network.ApiClient
import org.example.project.network.ImageManga


@Composable
fun ImageViewer(
    modifier: Modifier = Modifier,
    pagerState : PagerState,
    imgManga: List<ImageManga>
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .padding(top = 75.dp, bottom = 200.dp)
    ){ page ->

        val imgManga = imgManga[page]

        val imageLink = imgManga.imgLink

        val imageTitle = imgManga.imgTitle

        val headers = NetworkHeaders.Builder()
            .set("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:131.0) Gecko/20100101 Firefox/131.0")
            .set("Accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
            .set("Accept-Language", "en-GB,en;q=0.6")
            .set("Connection", "keep-alive")
            .set("Referer", "https://www.mangabats.com/")
            .set("Sec-Fetch-Dest", "image")
            .set("Sec-Fetch-Mode", "no-cors")
            .set("Sec-Fetch-Site", "cross-site")
            .set("Sec-Fetch-Storage-Access", "none")
            .set("Sec-GPC", "1")
            .set("Priority", "u=2, i")
            .set("Pragma", "no-cache")
            .set("Cache-Control", "no-cache")
            .build()


            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(imageLink)
                    .httpHeaders(headers)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxHeight()
                )

    }

}
@Composable
fun ChapterReader(
    chapterUrl: String,
    navController: NavController
){
    val fetchedItem  = remember { mutableStateOf<List<ImageManga>>(emptyList()) }

    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val fetchedItems = ApiClient.getChapterInfo(chapterUrl)
            fetchedItem.value = fetchedItems
            isLoading.value = false
            //Log.d("MangaNelo", "Fetched items: $fetchedItems")  //
        } catch (e:Exception) {
            //Log.e("MangaNelo", "error fetching chapter info",e)
            isLoading.value = false
        }
    }

    if (isLoading.value) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else if (fetchedItem.value.isEmpty()) {
        Text("No images Available")
    } else {
        val pagerState = rememberPagerState(pageCount = {
            fetchedItem.value.size
        })

        val imgManga = fetchedItem.value

        ImageViewer(modifier= Modifier,pagerState = pagerState, imgManga = imgManga)
    }

}