package org.example.project.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.network.ApiClient
import org.example.project.network.ImageManga
import org.example.project.network.fetchImageBytes
import org.example.project.network.toImageBitmap

enum class ReadingMode {
    HORIZONTAL_PAGER,
    WEBTOON,
    VERTICAL_SCROLL
}

data class ReaderSettings(
    val readingMode : ReadingMode = ReadingMode.HORIZONTAL_PAGER,
    val backgroundColor: Color = Color.Black,
    val showPageNumbers: Boolean = true,
    val autoHideUI: Boolean = true,
    val zoomEnabled: Boolean = true,
    val doubleTapToZoom: Boolean = true
)

@Composable
fun DisplayImage(
    modifier: Modifier = Modifier,
    imageManga: ImageManga,
    settings: ReaderSettings,
    onImageClick: () -> Unit = {},
    onImageDoubleTap: () -> Unit = {}
) {
    //val zoomState = rememberZoomState()
    var imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    var isLoading = remember { mutableStateOf(true) }
    var hasError = remember { mutableStateOf(false) }

    LaunchedEffect(imageManga.imgLink) {
        Napier.d("Starting image load: ${imageManga.imgLink}")

        isLoading.value = true
        hasError.value = false

        try {
            val bytes = fetchImageBytes(imageManga.imgLink)
            if (bytes != null) {
                imageBitmap.value = bytes.toImageBitmap()
                hasError.value = imageBitmap.value == null
            } else {
                hasError.value = true
            }
        } catch (e: Exception) {
            println("Error loading image: ${e.message}")
            hasError.value = true
        } finally {
            isLoading.value = false
        }
    }

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(settings.backgroundColor)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onImageClick() },
                        onDoubleTap = {
                            if (settings.doubleTapToZoom) {
                                onImageDoubleTap()
                            }
                        }
                    )
                }
        ) {
            when {
                isLoading.value -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                hasError.value -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Failed to load image", color = Color.White)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = {
                                isLoading.value = true
                                hasError.value = false
                            }) { Text("Retry") }
                        }
                    }
                }

                imageBitmap.value != null -> {
                    Image(
                        bitmap = imageBitmap.value!!,
                        contentDescription = imageManga.imgTitle,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
    }
}

@Composable
fun EnhancedImageViewer(
    modifier: Modifier = Modifier,
    imgManga: List<ImageManga>,
    settings: ReaderSettings,
    onPageChange: (Int) -> Unit = {},
    onImageClick: () -> Unit = {},
    onImageDoubleTap: () -> Unit = {}

) {


    when (settings.readingMode) {
        ReadingMode.HORIZONTAL_PAGER -> {
            val pagerState = rememberPagerState(pageCount = { imgManga.size })
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(pagerState.currentPage) {
                onPageChange(pagerState.currentPage + 1)
            }

            HorizontalPager(
                state = pagerState,
                modifier = modifier.fillMaxSize()
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    DisplayImage(
                        imageManga = imgManga[page],
                        settings = settings,
                        onImageClick = onImageClick,
                        onImageDoubleTap = onImageDoubleTap
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back button
                        if (page > 0) {
                            Button(onClick = {coroutineScope.launch {
                                pagerState.animateScrollToPage(page - 1)
                            }
                            }) {
                                Text("Previous")
                            }
                        } else {
                            Spacer(modifier = Modifier.width(100.dp)) // Keeps spacing
                        }

                        // Forward button
                        if (page < imgManga.lastIndex) {
                            Button(onClick = { coroutineScope.launch {
                                pagerState.animateScrollToPage(page + 1)
                            }
                            }) {
                                Text("Next")
                            }
                        } else {
                            Spacer(modifier = Modifier.width(100.dp)) // Keeps spacing
                        }
                    }
                }

            }
        }

        ReadingMode.VERTICAL_SCROLL -> {
            val listState = rememberLazyListState()

            LazyColumn(
                state = listState,
                modifier = modifier.fillMaxSize()
            ) {
                items(imgManga.size) { index ->
                    DisplayImage(
                        imageManga = imgManga[index],
                        settings = settings,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (index < imgManga.size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        ReadingMode.WEBTOON -> {
            val listState = rememberLazyListState()

            LaunchedEffect(listState) {
                snapshotFlow { listState.firstVisibleItemIndex }
                    .collect { index ->
                        onPageChange(index + 1)
                    }
            }

            LazyColumn(
                state = listState,
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(imgManga.size) { index ->
                    WebtoonImage(
                        imageManga = imgManga[index],
                        settings = settings,
                        onImageClick = onImageClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun WebtoonImage(
    modifier: Modifier = Modifier,
    imageManga: ImageManga,
    settings: ReaderSettings,
    onImageClick: () -> Unit = {}
) {
    var bitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    var isLoading = remember { mutableStateOf(true) }
    var hasError = remember { mutableStateOf(false) }
    var imageHeight = remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    //val screenWidth = LocalPlatformContext.current.screenWidthDp.dp

    BoxWithConstraints (
        modifier = modifier
            .fillMaxWidth()
            .background(settings.backgroundColor)
            .clickable{ onImageClick() }
    ){
        val screenWidth = maxWidth
        val httpClient = remember {
            HttpClient(CIO) {
                install(HttpTimeout) {
                    requestTimeoutMillis = 30000
                    connectTimeoutMillis = 30000
                    socketTimeoutMillis = 60000
                }
                install(HttpRequestRetry) {
                    retryOnServerErrors(maxRetries = 3)
                    retryOnException(maxRetries = 3, retryOnTimeout = true)
                    exponentialDelay()
                }

                /*
                engine {
                    https {
                        trustManager = object : X509TrustManager {
                            @SuppressLint("TrustAllX509TrustManager")
                            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                            @SuppressLint("TrustAllX509TrustManager")
                            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                        }
                    }
                }*/

                defaultRequest {
                    header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:131.0) Gecko/20100101 Firefox/131.0")
                    header("Accept", "image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5")
                    header("Accept-Language", "en-GB,en;q=0.5")
                    header("Connection", "keep-alive")
                    header("Referer", "https://www.mangabats.com/")
                    header("Sec-Fetch-Dest", "image")
                    header("Sec-Fetch-Mode", "no-cors")
                    header("Sec-Fetch-Site", "cross-site")
                    header("Priority", "u=5, i")
                    header("Pragma", "no-cache")
                    header("Cache-Control", "no-cache")
                }
            }
        }

        LaunchedEffect(imageManga.imgLink) {
            isLoading.value = true
            hasError.value = false
            bitmap.value = null

            try {
                delay(100)
                val response: HttpResponse = httpClient.get(imageManga.imgLink)

                Napier.d("HTTP status: ${response.status.value} for URL: ${imageManga.imgLink}")

                if (response.status.isSuccess()) {
                    val imageBytes = response.body<ByteArray>()

                    Napier.d("Downloaded bytes: ${imageBytes.size}")

                    val loadedBitmap = imageBytes.toImageBitmap()

                    if (loadedBitmap != null) {
                        bitmap.value = loadedBitmap

                        with(density) {
                            val aspectRatio = loadedBitmap.height.toFloat() / loadedBitmap.width.toFloat()
                            imageHeight.value = screenWidth * aspectRatio
                        }

                        isLoading.value = false
                    } else {
                        hasError.value = true
                        isLoading.value = false
                        Napier.e("Failed to decode bitmap from: ${imageManga.imgLink}")

                        //Log.e("WebtoonImage", "Failed to decode bitmap from: ${imageManga.imgLink}")
                    }
                } else {
                    hasError.value = true
                    isLoading.value = false
                    Napier.e("HTTP error ${response.status.value} for: ${imageManga.imgLink}")

                    //Log.e("WebtoonImage", "HTTP error ${response.status.value} for: ${imageManga.imgLink}")
                }
            } catch (e: Exception) {
                hasError.value = true
                isLoading.value = false
                Napier.e("Error loading image: ${imageManga.imgLink}", e)

                //Log.e("WebtoonImage", "Error loading image: ${imageManga.imgLink}", e)
            }
        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .then(
                    if (imageHeight.value > 0.dp) {
                        Modifier.height(imageHeight.value)
                    } else {
                        Modifier.heightIn(min = 200.dp)
                    }
                )
                .background(settings.backgroundColor)
                .clickable { onImageClick() }
        ) {
            when {
                isLoading.value -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    }
                }

                hasError.value -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Failed to load image",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    isLoading.value = true
                                    hasError.value = false
                                }
                            ) {
                                Text("Retry", fontSize = 12.sp)
                            }
                        }
                    }
                }

                bitmap.value != null -> {
                    Image(
                        bitmap = bitmap.value!!,
                        contentDescription = imageManga.imgTitle,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        }

    }


}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterReader(
    modifier: Modifier = Modifier,
    chapterUrl: String,
    navController: NavController
){

    var isUIVisible = remember { mutableStateOf(false) }
    var currentPage = remember { mutableIntStateOf(1) }
    var settings = remember { mutableStateOf(ReaderSettings()) }
    var showSettings = remember { mutableStateOf(false) }
    var fetchedImages = remember { mutableStateOf<List<ImageManga>>(emptyList()) }
    //var isLoading = remember { mutableStateOf(true) }
    var errorMessage = remember { mutableStateOf<String?>(null) }

    val fetchedItem  = remember { mutableStateOf<List<ImageManga>>(emptyList()) }

    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val fetchedItems = ApiClient.getChapterInfo(chapterUrl)
            fetchedItem.value = fetchedItems
            fetchedImages.value = fetchedItems
            isLoading.value = false
            //Log.d("MangaNelo", "Fetched items: $fetchedItems")  //
        } catch (e:Exception) {
            //Log.e("MangaNelo", "error fetching chapter info",e)
            isLoading.value = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()) {
        when {
            isLoading.value -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 4.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading chapter...",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            errorMessage.value != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.8f))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = Color.Red,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = errorMessage.value ?: "Failed to load chapter",
                                color = Color.White,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { navController.popBackStack() }
                            ) {
                                Text("Go Back")
                            }
                        }
                    }
                }
            }

            fetchedItem.value.isNotEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    EnhancedImageViewer(
                        imgManga = fetchedImages.value,
                        settings = settings.value,
                        onPageChange = { page -> currentPage.intValue = page },
                        onImageClick = { isUIVisible.value = !isUIVisible.value },
                        onImageDoubleTap = {

                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (isUIVisible.value) {
                        TopAppBar(
                            title = {
                                Text(text = "Back")
                            },
                            navigationIcon = {
                                IconButton(onClick = {navController.popBackStack()}) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            },
                            actions = {
                                // Settings button
                                IconButton(onClick = { showSettings.value = !showSettings.value }) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Reading Mode Settings"
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )

                        if (showSettings.value) {
                            Card(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 64.dp)
                                    .zIndex(2f),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Black.copy(alpha = 0.9f)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Reading Mode",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )

                                    // Reading mode buttons
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        ReadingMode.entries.forEach { mode ->
                                            val isSelected = settings.value.readingMode == mode
                                            Button(
                                                onClick = {
                                                    settings.value = settings.value.copy(readingMode = mode)
                                                    showSettings.value = false
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary
                                                    else Color.Gray
                                                )
                                            ) {
                                                Text(
                                                    text = when (mode) {
                                                        ReadingMode.HORIZONTAL_PAGER -> "Pages"
                                                        ReadingMode.VERTICAL_SCROLL -> "Scroll"
                                                        ReadingMode.WEBTOON -> "Webtoon"
                                                    },
                                                    fontSize = 12.sp,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        BottomAppBar(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .zIndex(1f)
                        ) {
                            Text("Page ${currentPage.intValue}", modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }
        }
    }

}