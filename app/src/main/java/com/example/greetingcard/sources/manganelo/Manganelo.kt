package com.example.greetingcard.sources.manganelo

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.greetingcard.database.Manga
import com.example.greetingcard.models.LatestManga
import com.example.greetingcard.pages.ImageCard
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets



@Composable
fun StyledTextField(viewModel: MainSearchModel) {
    var value by remember { mutableStateOf("") }

    val searchQuery by remember { viewModel.searchQuery }

    TextField(
        value = value,
        onValueChange = { newValue ->
            value = newValue
        },
        label = { Text("Search Manga") },
        placeholder = { Text("Type a title...") },
        maxLines = 1,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (value.isNotBlank()) {
                    viewModel.fetchMangasSearch(value)
                }
            }
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}


@OptIn(ExperimentalCoilApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MangaNelo(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val viewModel :MainSearchModel = viewModel()

    val searchQuery by remember { viewModel.searchQuery }

    val isLoading by remember { viewModel.isLoading }
    val errorMessage by remember { viewModel.errorMessage }

    val mangas by remember { viewModel.mangas }

    val itemsList = remember { mutableStateOf<List<LatestManga>>(emptyList()) }

    LaunchedEffect(Unit) {
        viewModel.fetchLatest()
    }

    Column {
        Row (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .padding(  25.dp),
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
                        text = "Mangabat",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    StyledTextField(viewModel)
                }



                Row {
                    ElevatedButton(
                        onClick = {
                            viewModel.fetchPopular()
                        }) {
                        Text("Popular Manga")
                    }
                    ElevatedButton(
                        onClick = {
                            viewModel.fetchLatest()
                        }) {
                        Text("Latest Manga")
                    }
                }

            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                //.padding(bottom = 90.dp)
        ) {
            items(mangas) { item ->

                val imageUrl = item.cover
                val title = item.title
                val description = "sunny"
                val mangaUrl = item.manga_url

                val imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .addHeader(
                        "User-Agent",
                        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"
                    )
                    .addHeader("Referer", "https://www.mangabats.com/")
                    .removeHeader("Connection")
                    .build()

                val painter = rememberImagePainter(imageRequest)
                val encodedMangaUrl = URLEncoder.encode(mangaUrl, StandardCharsets.UTF_8.toString())

                Box(modifier = Modifier
                    //.fillMaxWidth(0.5f)
                    .padding(5.dp)
                ){
                    val manga = Manga(title, cover = imageUrl, mangaUrl = mangaUrl)
                    val gson = Gson()
                    val mangaJson = gson.toJson(manga)

                    ImageCard(
                        painter = painter,
                        contentDescription = description,
                        title = title,
                        contentScale = ContentScale.FillBounds,
                        onClick = {
                            navController.navigate("itemDetail/${encodedMangaUrl}")
                        }
                    )
                }
            }
        }
    }
    }



