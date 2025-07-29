package com.example.greetingcard.pages

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.greetingcard.database.MangaViewModel
import com.example.greetingcard.requests.RetrofitClient

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationPage(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    val mangaViewModel: MangaViewModel = hiltViewModel()

    var notificationText by remember { mutableStateOf("Loading...") }


    LaunchedEffect(Unit) {
        try {
            Log.d("KtorClient", "Attempting to fetch hello message...")
            val text = RetrofitClient.apiService.getHelloMessage()
            Log.d("KtorClient", "Received: $text")
            notificationText = text
        } catch (e: Exception) {
            Log.e("KtorClient", "Error fetching message: ${e.message}", e)
            notificationText = "Error: ${e.message}"
        }
    }

    Scaffold(
        //topBar = {
          //  SmallTopAppBarExample(
          //      "Sources"
            //)
        //}
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = innerPadding.calculateBottomPadding()
            )
        ) {

            item {
                Row (
                    Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(start = 25.dp)
                        .height(50.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,

                    ){

                    val imageUrl = "https://www.mangabats.com/images/favicon-bat.webp"

                    Text(
                        text = "01"
                    )

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Translated description of what the image contains",
                        modifier
                            .padding(horizontal = 25.dp)
                            .height(25.dp)
                            .width(25.dp)
                    )

                    Text(
                        text = "MangaBat",
                        modifier = Modifier
                            .clickable {

                                notificationText = ""

                                navController.navigate("detail/Manganelo")
                            }
                        /*fontSize = 40.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White*/
                    )

                }

            }
        }

    }



}


