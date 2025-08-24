package com.example.greetingcard.pages

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.util.TableInfo
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.greetingcard.database.MangaViewModel
import com.example.greetingcard.sources.manganelo.ChapterReader
import com.example.greetingcard.sources.manganelo.ItemDetail

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBarExample(
    title: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = {
                    Box(
                        Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },

    ){}
}


@OptIn(ExperimentalCoilApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    mangaViewModel: MangaViewModel = viewModel(),
    navController: NavHostController
) {

            Column (
                modifier = Modifier
                    .padding(bottom = 0.dp, top = 25.dp)
            ){
                Row (modifier = modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .height(75.dp),
                ){

                    SmallTopAppBarExample("Library")

                }



                val allMangas by mangaViewModel.allMangas.observeAsState(initial = emptyList())

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .padding(bottom = 0.dp)
                ) {
                    items(allMangas) { manga ->

                        val imageRequest = ImageRequest.Builder(LocalContext.current)
                            .data(manga.cover)
                            .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:131.0) Gecko/20100101 Firefox/131.0")
                            .addHeader("Accept", "image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5")
                            .addHeader("Accept-Language", "en-GB,en;q=0.5")
                            .addHeader("Connection", "keep-alive")
                            .addHeader("Referer", "https://www.mangabats.com/")
                            .addHeader("Sec-Fetch-Dest", "image")
                            .addHeader("Sec-Fetch-Mode", "no-cors")
                            .addHeader("Sec-Fetch-Site", "cross-site")
                            .addHeader("Priority", "u=5, i")
                            .addHeader("Pragma", "no-cache")
                            .addHeader("Cache-Control", "no-cache")
                            .build()

                        val painter = rememberImagePainter(imageRequest)

                        val encodedMangaUrl = Uri.encode(manga.mangaUrl)

                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                        ) {
                            ImageCard(
                                painter = painter,
                                contentDescription = "devil",
                                title = manga.name,
                                onClick = {
                                    navController.navigate("itemDetail/${encodedMangaUrl}")
                                }
                            )
                        }

                    }
                }


            }
        }

@RequiresApi(Build.VERSION_CODES.O)
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
