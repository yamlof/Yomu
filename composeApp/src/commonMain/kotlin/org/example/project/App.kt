package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.example.project.database.MangaViewModel
import org.example.project.di.commonModule
import org.example.project.pages.ChapterReader
import org.example.project.pages.ChapterUrl
import org.example.project.pages.HomePage
import org.example.project.pages.ItemDetail
import org.example.project.pages.MangaUrl
import org.example.project.pages.SettingsPage
import org.example.project.pages.SourceNavigation
import org.example.project.pages.SourcePage
import org.example.project.pages.SourcePage
import org.example.project.source.MangaBat
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.KoinApplication


@Composable
@Preview
fun App() {

    MaterialTheme (
        typography = jetBrainsMonoTypography()
    ){
        val navItemList = listOf(
            NavItem("Home", Icons.Filled.Home,0,"home"),
            NavItem("Sources", Icons.Default.Notifications,0,"sources"),
            NavItem("Settings", Icons.Default.Settings,0,"settings")
        )

        val navController = rememberNavController()

            val selectedIndex = remember { mutableStateOf(0) }

            val viewModel = koinViewModel<MangaViewModel>()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar {
                    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
                    navItemList.forEach { navItem ->
                        val selected = currentDestination?.route == navItem.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(navItem.route) {
                                    // Avoid multiple copies of the same destination
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                BadgedBox(badge = {
                                    if (navItem.badgeCount > 0)
                                        Badge { Text(text = navItem.badgeCount.toString()) }
                                }) {
                                    Icon(navItem.icon, contentDescription = navItem.label)
                                }
                            },
                            label = { Text(navItem.label) }
                        )
                    }
                }
            }
        ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("home") { HomePage(viewModel, Modifier, navController) }
                    composable("sources") { SourcePage(viewModel,navController) }
                    composable("settings") { SettingsPage() }

                    composable<SourceNavigation> {
                        val source: SourceNavigation = it.toRoute()
                        MangaBat(modifier = Modifier, navController,source.url)
                    }
                    composable<MangaUrl> {
                        val mangaUrl: MangaUrl = it.toRoute()
                        ItemDetail(viewModel, mangaUrl.url, navController)
                    }
                    composable<ChapterUrl> {
                        val chapterUrl: ChapterUrl = it.toRoute()
                        ChapterReader(chapterUrl.url, navController)
                    }
                }
            }

        }





    }


@Composable
fun ContentScreen(
    viewModel: MangaViewModel,
    modifier: Modifier,
    selectedIndex:Int,
    navController : NavController,
) {
    when(selectedIndex) {
        0 -> HomePage(viewModel,modifier = modifier, navController = navController)
        1 -> SourcePage(viewModel = viewModel,navController )
        2 -> SettingsPage()
    }
}