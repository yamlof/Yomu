package org.example.project.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.example.project.network.ApiClient
import org.example.project.network.LatestManga
import org.example.project.source.ImageCard
import androidx.compose.ui.graphics.Color
import org.example.project.database.MangaEntity
import org.example.project.database.MangaViewModel
import org.example.project.network.Chapter

@Composable
fun MangaInformation(
    viewModel: MangaViewModel,
    title: String?,
    author: String?,
    status: String?,
    cover: String?,
    url: String?,
    modifier: Modifier = Modifier
) {

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(title ?: "Loading...", style = MaterialTheme.typography.titleMedium)
        Text(author ?: "Unknown", style = MaterialTheme.typography.bodyMedium)
        Text(status ?: "Unknown", style = MaterialTheme.typography.bodyMedium)

        val newManga = MangaEntity(
            name = title.toString(),
            cover = cover.toString(),
            mangaUrl = url.toString()
        )

        val library =  viewModel.allMangas.collectAsState()
        val isInLibrary = library.value.any({ it.name == newManga.name })

// Debug print
        LaunchedEffect(library) {
            println("Current library: $library")
        }

        ElevatedButton(onClick = {
            viewModel.addManga(newManga)

        }) {
            Text(
                text = if (isInLibrary) "Remove from Library" else "Add to Library"
            )
        }
    }
}

@Composable
fun MangaCover(
    cover: String?,
    title: String?,
    modifier: Modifier = Modifier
){
    ImageCard(
        model = cover?: "",
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(3f/4f)
            .padding(8.dp),
        contentDescription = title ?: "MangaCover",
        title = title ?: "Loading...",
        onClick = {}
    )
}

@Composable
fun ItemDetail(
    viewModel: MangaViewModel,
    mangaUrl: String?,
    navController: NavController
){
    val itemsList = remember { mutableStateOf<List<LatestManga>>(emptyList()) }

    Column {

        val fetcheditem  = remember { mutableStateOf<String?>(null) }
        val fetchedTitle = remember { mutableStateOf<String?>(null) }
        val fetchedStatus = remember { mutableStateOf<String?>(null) }
        val fetchedAuthor = remember { mutableStateOf<String?>(null) }
        val fetchedChapters = remember {mutableStateOf<List<Chapter>>(emptyList()) }

        LaunchedEffect(Unit) {
            val fetchedItems = ApiClient.getMangaInfo(mangaUrl.toString())
           // Log.d("MangaNelo", "Fetched items: $fetchedItems")
            fetcheditem.value = fetchedItems.cover
            fetchedTitle.value = fetchedItems.title
            fetchedAuthor.value = fetchedItems.author
            fetchedStatus.value = fetchedItems.status
            fetchedChapters.value = fetchedItems.chapters
        }

        OutlinedCard(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            border = BorderStroke(1.dp, Color(blue = 1, red = 2, green = 3)),
            modifier = Modifier
                .height( height = 280.dp)
                .fillMaxWidth()
                .padding(top = 25.dp),
            shape = RectangleShape
        ) {
            BoxWithConstraints (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ){
                val isCompact = maxWidth < 400.dp

                if (isCompact) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ){
                        MangaCover(
                            cover = fetcheditem.value,
                            title = fetchedTitle.value,
                            modifier = Modifier
                                .width(120.dp)               // fixed width
                                .aspectRatio(2f / 3f)        // keep manga/book proportions
                                .padding(vertical = 20.dp)
                        )

                        MangaInformation(
                            viewModel = viewModel,
                            title = fetchedTitle.value,
                            author = fetchedAuthor.value,
                            status = fetchedStatus.value,
                            cover = fetcheditem.value,
                            url = mangaUrl
                        )
                    }
                } else {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ){

                        Box(
                            modifier = Modifier
                                //.fillMaxWidth()
                                .height(120.dp)
                        ){
                            MangaCover(
                                cover = fetcheditem.value,
                                title = fetchedTitle.value,
                                modifier = Modifier
                                    .width(180.dp)
                                    .aspectRatio(2f / 3f)
                                    .padding(top = 40.dp)
                            )
                        }


                        MangaInformation(
                            viewModel = viewModel,
                            title = fetchedTitle.value,
                            author = fetchedAuthor.value,
                            status = fetchedStatus.value,
                            cover = fetcheditem.value,
                            url = mangaUrl,
                            modifier = Modifier.weight(1f)
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

                val chapterName = item.title
                val chapterLink = item.url

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clickable {
                            navController.navigate(route = ChapterUrl(chapterLink))
                        },
                    shape = RectangleShape,
                    colors = CardDefaults.cardColors(Color(0xFFA4C2D7)),
                ) {
                    Text(
                        text = chapterName ?: "Loading Title",
                        style = MaterialTheme.typography.titleMedium,

                    )
                }
            }
            item {
                Text(text = "Last item")
            }
        }

    }
}