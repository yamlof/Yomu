package org.example.project.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.compose.AsyncImage
import kotlinx.serialization.Serializable
import org.example.project.source.MangaBat
import io.ktor.http.encodeURLParameter
import io.ktor.http.decodeURLQueryComponent

fun String.encodeForNav(): String = this.encodeURLParameter()
fun String.decodeFromNav(): String = this.decodeURLQueryComponent()

@Serializable
data class DetailRoute(
    val source: String
)

/*
enum class CupcakeScreen(val title: StringResource) {
    Start(title = Res.string.app_name),
    Flavor(title = Res.string.choose_flavor),
    Pickup(title = Res.string.choose_pickup_date),
    Summary(title = Res.string.order_summary)
}*/

@Serializable
data class MangaUrl (val url: String)

@Serializable
data class ChapterUrl (val url : String)

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
                    Row (
                        Modifier
                            .background(color = MaterialTheme.colorScheme.background)
                            .padding(start = 25.dp)
                            .height(50.dp)
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(route = "detail/MangaBat")
                            },
                        verticalAlignment = Alignment.CenterVertically,

                        ){

                        val imageUrl = "https://www.mangabats.com/images/favicon-bat.webp"

                        Text(
                            text = "01"
                        )

                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Translated description of what the image contains",
                            modifier = Modifier
                                .padding(horizontal = 25.dp)
                                .height(25.dp)
                                .width(25.dp)
                        )
                        Text(
                            text = "MangaBat",
                            modifier = Modifier

                            /*fontSize = 40.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White*/
                        )
                }
            }
        }


        composable("detail/MangaBat") { //backStackEntry ->
            //val args = backStackEntry.toRoute<DetailRoute>()
            MangaBat(navController = navController)
        }


        composable<MangaUrl>{ navBackStackEntry ->

            val mangaUrl : MangaUrl = navBackStackEntry.toRoute()

            ItemDetail(mangaUrl = mangaUrl.url, navController = navController)
        }

        composable<ChapterUrl> { navBackStackEntry ->
            val chapterUrl : ChapterUrl = navBackStackEntry.toRoute()

            ChapterReader(chapterUrl = chapterUrl.url,navController= navController)
        }
        /*

        composable("chapter/{chapterUrl}") { navBackStackEntry ->
            val chapterName = navBackStackEntry.arguments?.getString("chapterUrl")
            if (chapterName != null) {
                ChapterReader(chapterUrl = chapterName, navController = navController)
            }
        }*/
    }


}
}