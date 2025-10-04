package org.example.project.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.project.database.MangaViewModel
import org.example.project.jetBrainsMonoTypography
import org.example.project.source.ImageCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBarExample() {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = "LIBRARY", style = MaterialTheme.typography.headlineMedium)


                }
            )
        },
    ){}
}

@Composable
fun HomePage(
    viewModel: MangaViewModel,
    modifier: Modifier,

) {

    val navController = rememberNavController()
    val allMangas by viewModel.allMangas.collectAsState()

    LaunchedEffect(Unit) {
        println("Entering Library! Current mangas: $allMangas")
    }


        NavHost(
            navController = navController,
            startDestination = "library",
            modifier = Modifier.padding()
        ) {
            composable("library") {



                SmallTopAppBarExample()

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    val isCompact = maxWidth < 600.dp
                    val columns = if (isCompact) 2 else 4
                    val itemPadding = if (isCompact) 6.dp else 10.dp
                    val gridPadding = if (isCompact) 12.dp else 24.dp

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columns),
                        contentPadding = PaddingValues(gridPadding),
                        modifier = Modifier
                            .padding(bottom = 90.dp)
                    ) {
                        items(allMangas) { manga ->

                            //val manga = allMangas[index]

                            val imageUrl = manga.cover
                            val title = manga.name
                            val description = "sunny"
                            val mangaUrl = manga.mangaUrl

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
    }
