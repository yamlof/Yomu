package org.example.project.source

import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.seiko.imageloader.rememberImagePainter
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import org.example.project.network.ApiClient
import org.example.project.network.LatestManga
import io.ktor.http.encodeURLQueryComponent
import org.example.project.pages.MangaUrl


/*
@Composable
fun StyledTextField() {

    var value by remember { mutableStateOf("") }

    val searchQuery by remember { viewModel.searchQuery }

    TextField(
        value = value,
        onValueChange = { newValue:String ->
            value = newValue
        },
        //label = { Text("MangaNelo") },
        maxLines = 1,
        textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
        modifier = Modifier
            .padding(20.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                if (value.isNotBlank()) {
                    viewModel.fetchMangasSearch(value)
                }
                //LocalSoftwareKeyboardController.current?.hide()
            }
        )
    )
}*/

@Composable
fun ImageCard(
    model: Any, // URL or resource
    contentDescription: String?,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(model)
                    .httpHeaders(headers)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f) // common manga cover ratio
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Fit,

            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = title,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                maxLines = 2
            )
        }
    }
}


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

@Composable
fun MangaBat(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    source : String
) {

    val mangaLatest = remember { mutableStateOf<List<LatestManga>>(emptyList()) }


    LaunchedEffect(Unit) {
        mangaLatest.value = ApiClient.getLatest()
    }

    Column {
        Row (
            modifier = Modifier
                .background(color = Color.White)
                .fillMaxWidth()
                .padding(25.dp),
            Arrangement.Center
        ){
            Column(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceEvenly
            ) {
                Row {
                    Text(
                        modifier = Modifier
                            .padding(top = 40.dp)
                        ,
                        text = "MANGABAT"
                    )

                }

            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            val isCompact = maxWidth < 600.dp
            val isWide = maxWidth > 840.dp
            val columns = when {
                isCompact -> 2
                isWide -> 8
                else -> 4
            }
            val itemPadding = if (isCompact) 6.dp else 10.dp
            val gridPadding = if (isCompact) 12.dp else 24.dp

            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                contentPadding = PaddingValues(gridPadding),
                modifier = Modifier
                    .padding(bottom = 90.dp)
            ) {
                items(mangaLatest.value.size) { index ->

                    val manga = mangaLatest.value[index]

                    val imageUrl = manga.cover
                    val title = manga.title
                    val description = "sunny"
                    val mangaUrl = manga.manga_url

                    Box(modifier = Modifier
                        //.fillMaxWidth(0.5f)
                        .padding(5.dp)
                    ){
                        ImageCard(
                            model = imageUrl,
                            contentDescription = "devil",
                            title = title,
                            onClick = {
                                navController.navigate(route = MangaUrl(mangaUrl))
                            }
                        )
                    }
                }
            }
        }


    }
}
