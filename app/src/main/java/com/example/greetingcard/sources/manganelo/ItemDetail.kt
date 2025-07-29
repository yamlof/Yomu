package com.example.greetingcard.sources.manganelo

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.greetingcard.database.Manga
import com.example.greetingcard.database.MangaViewModel
import com.example.greetingcard.models.Chapter
import com.example.greetingcard.models.LatestManga
import com.example.greetingcard.pages.ImageCard
import com.example.greetingcard.requests.RetrofitClient




@OptIn(ExperimentalCoilApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemDetail(
    modifier: Modifier = Modifier,
    mangaJson:String,
    navController: NavController,
    viewModel: MangaViewModel
) {
    val itemsList = remember { mutableStateOf<List<LatestManga>>(emptyList()) }

    Column (
        modifier = Modifier
            .padding(bottom = 75.dp)
    ){
        val fetcheditem  = remember { mutableStateOf<String?>(null) }
        val fetchedTitle = remember { mutableStateOf<String?>(null) }
        val fetchedStatus = remember { mutableStateOf<String?>(null) }
        val fetchedAuthor = remember { mutableStateOf<String?>(null) }
        val fetchedChapters = remember {mutableStateOf<List<Chapter>>(emptyList()) }

        var showErrorDialog = remember { mutableStateOf(false) }
        var retryTrigger = remember { mutableStateOf(0) }


        LaunchedEffect(retryTrigger) {
            try {
                val fetchedItems = RetrofitClient.apiService.getMangaInfo(mangaJson)
                Log.d("MangaNelo", "Fetched items: $itemsList")  //
                fetcheditem.value = fetchedItems.cover
                fetchedTitle.value = fetchedItems.title
                fetchedAuthor.value = fetchedItems.author
                fetchedStatus.value = fetchedItems.status
                fetchedChapters.value = fetchedItems.chapters
            } catch (e: Exception) {
                showErrorDialog.value = true
            }

        }

        if (showErrorDialog.value) {
            AlertDialog(
                onDismissRequest = {showErrorDialog.value=false},
                confirmButton = {
                    TextButton(onClick = {
                        retryTrigger.value++ // Triggers fetch again
                    }) {
                        Text("Retry")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showErrorDialog.value = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Error") },
                text = { Text("Failed to load manga info.This might be because the server is asleep.Try again in 60 seconds") }
            )
        }

        OutlinedCard(
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier
                .height(height = 280.dp)
                .fillMaxWidth()
                .padding(top = 25.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            val description = "Sunny"
            Row (
                modifier = modifier
                    .height(600.dp)
                    .padding(5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ){
                val cover = fetcheditem.value

                val imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(cover)
                    .addHeader(
                        "User-Agent",
                        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"
                    )
                    .addHeader("Referer", "https://www.mangabats.com/")
                    .removeHeader("Connection") // Let OkHttp handle this
                    .build()

                val painter = rememberImagePainter(imageRequest)
                ImageCard(
                    painter = painter,
                    modifier = Modifier
                        .fillMaxHeight()
                        .height(25.dp)
                        .size(height = 20.dp, width = 120.dp)
                        .padding(top = 20.dp, bottom = 20.dp),
                    contentDescription = description,
                    contentScale = ContentScale.FillBounds,
                    title = fetchedTitle.value ?: "Loading title...",
                    onClick = {}
                )

                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .padding(start = 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = fetchedTitle.value ?: "Loading title...",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = fetchedAuthor.value ?: "Loading author...",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = fetchedStatus.value ?: "Loading status...",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    val newManga = Manga(
                        name = fetchedTitle.value ?: "Not Found Yet",
                        cover = cover ?: "Not Found Yet",
                        mangaUrl = mangaJson
                    )

                    val library = viewModel.allMangas.observeAsState()
                    val isInLibrary = library.value?.any { it.name == newManga.name } == true

                    ElevatedButton(
                        onClick = {
                            val storedManga = library.value?.find { it.name == newManga.name }
                            if (isInLibrary) {
                                if (storedManga != null) {
                                    viewModel.deleteManga(storedManga.name)
                                }
                            } else {
                                viewModel.addManga(newManga)
                            }
                        },
                        modifier = Modifier.padding(top = 25.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            if (isInLibrary) "Remove from Library" else "Add to Library",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

            }

        }
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
        ){
            items(fetchedChapters.value) { item ->

                val chapterName = item.chapterTitle
                val chapterLink = item.chapterLink

                val encodedChapterUrl = Uri.encode(chapterLink)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clickable {
                            navController.navigate("chapter/$encodedChapterUrl")
                        },
                    //shape = MaterialTheme.shapes.small,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                ) {
                    Text(
                        text = chapterName ?: "Loading Title",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

        }
    }
}