package org.example.project.source

import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import coil3.network.NetworkHeaders
import coil3.request.ImageRequest
import com.seiko.imageloader.rememberImagePainter
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import org.example.project.network.ApiClient
import org.example.project.network.LatestManga
import io.ktor.http.encodeURLQueryComponent


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
    painter: Painter,
    contentDescription: String,
    title:String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.surface
        )
    ){
        Box(modifier = Modifier.height(250.dp)) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                        startY = 300f
                    )
                )
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(title,style = androidx.compose.ui.text.TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                )
                )
            }
        }
    }
}


val headers = NetworkHeaders.Builder()
    .set("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:131.0) Gecko/20100101 Firefox/131.0")
    .set("Accept", "image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5")
    .set("Accept-Language", "en-GB,en;q=0.5")
    .set("Connection", "keep-alive")
    .set("Referer", "https://chapmanganelo.com/")
    .set("Sec-Fetch-Dest", "image")
    .set("Sec-Fetch-Mode", "no-cors")
    .set("Sec-Fetch-Site", "cross-site")
    .set("Priority", "u=5, i")
    .set("Pragma", "no-cache")
    .set("Cache-Control", "no-cache")
    .build()

@Composable
fun MangaNelo(
    modifier: Modifier = Modifier,
    navController: NavHostController
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
                        text = "MangaNelo"
                    )

                }

            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .padding(bottom = 90.dp)
        ) {
            items(mangaLatest.value.size) { index ->

                val manga = mangaLatest.value[index]

                val imageUrl = manga.cover
                val title = manga.title
                val description = "sunny"
                val mangaUrl = manga.manga_url

                val painter = rememberImagePainter(mangaUrl)

                Box(modifier = Modifier
                    //.fillMaxWidth(0.5f)
                    .padding(5.dp)
                ){
                    ImageCard(
                        painter = painter,
                        contentDescription = "devil",
                        title = title,
                        onClick = {

                        }
                    )
                }
            }
        }
    }
}
