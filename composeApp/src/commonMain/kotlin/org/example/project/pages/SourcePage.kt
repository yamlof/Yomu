package org.example.project.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun SourcePage(

){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list"){
        composable("list") {
            LazyColumn {
                item {
                    Text(
                        text = "",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
                item {
                    Text(
                        text = "Manganelo",
                        modifier = Modifier
                            .clickable {
                                navController.navigate("detail/Manganelo")
                            }
                        /*fontSize = 40.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White*/
                    )
                }
            }
        }
        /*
        composable("detail/{source}") { navBackStackEntry ->
            val source = navBackStackEntry.arguments?.getString("source")
            MangaNelo(navController =navController)
        }

        composable("SourceItemDetail/{manga_url}"){ navBackStackEntry ->
            val itemName = navBackStackEntry.arguments?.getString("manga_url")
            if (itemName != null) {
                ItemDetail(mangaJson = itemName, navController = navController, viewModel = mangaViewModel)
            }
        }

        composable("chapter/{chapterUrl}") { navBackStackEntry ->
            val chapterName = navBackStackEntry.arguments?.getString("chapterUrl")
            if (chapterName != null) {
                ChapterReader(chapterUrl = chapterName, navController = navController)
            }
        }*/
    }


}