package org.example.project.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.compose.AsyncImage
import kotlinx.serialization.Serializable
import org.example.project.source.MangaBat
import io.ktor.http.encodeURLParameter
import io.ktor.http.decodeURLQueryComponent
import org.example.project.database.MangaViewModel

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

@Serializable
data class SourceNavigation(val url: String = "MangaBat")

@Composable
fun SourcePage(
    viewModel: MangaViewModel,
    navController : NavController
){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(SourceNavigation("MangaBat"))
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text( text = "01" )

        AsyncImage(
            model = "https://www.mangabats.com/images/favicon-bat.webp",
            contentDescription = "",
            modifier = Modifier
                .padding(horizontal = 25.dp)
                .height(25.dp)
                .width(25.dp)
        )
        Text("MANGABAT")
    }


}