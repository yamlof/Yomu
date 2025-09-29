package org.example.project.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.seiko.imageloader.rememberImagePainter
import org.example.project.network.ApiClient
import org.example.project.network.LatestManga
import org.example.project.source.ImageCard
import androidx.compose.ui.graphics.Color

@Composable
fun MangaInformation(
    title: String?,
    author:String?,
    status:String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(title ?: "Loading...", style = MaterialTheme.typography.titleMedium)
        Text(author ?: "Unknown", style = MaterialTheme.typography.bodyMedium)
        Text(status ?: "Unknown", style = MaterialTheme.typography.bodyMedium)

        ElevatedButton(onClick = { /* add to library */ }) {
            Text("Add to Library")
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
    mangaUrl: String?,
    navController: NavController
){
    val itemsList = remember { mutableStateOf<List<LatestManga>>(emptyList()) }

    Column {
        if (mangaUrl != null) {
            Text(mangaUrl)
        }

        val fetcheditem  = remember { mutableStateOf<String?>(null) }
        val fetchedTitle = remember { mutableStateOf<String?>(null) }
        val fetchedStatus = remember { mutableStateOf<String?>(null) }
        val fetchedAuthor = remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            val fetchedItems = ApiClient.getMangaInfo(mangaUrl.toString())
           // Log.d("MangaNelo", "Fetched items: $fetchedItems")
            fetcheditem.value = fetchedItems.cover
            fetchedTitle.value = fetchedItems.title
            //fetchedAuthor.value = fetchedItems.author
            //fetchedStatus.value = fetchedItems.status
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
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ){
                        MangaCover(
                            cover = fetcheditem.value,
                            title = fetchedTitle.value
                        )

                        MangaInformation(
                            title = fetchedTitle.value,
                            author = fetchedAuthor.value,
                            status = fetchedStatus.value
                        )
                    }
                } else {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ){
                        MangaCover(
                            cover = fetcheditem.value,
                            title = fetchedTitle.value,
                            modifier = Modifier.weight(1f)
                        )

                        MangaInformation(
                            title = fetchedTitle.value,
                            author = fetchedAuthor.value,
                            status = fetchedStatus.value,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

            }



        }

    }
}