package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
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
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navItemList = listOf(
                NavItem("Home", Icons.Filled.Home,0,"home"),
                NavItem("Sources", Icons.Default.Notifications,0,"sources"),
                NavItem("Settings", Icons.Default.Settings,0,"settings")
            )

            val navController = rememberNavController()

            Napier.base(DebugAntilog())

            val currentDestination = navController
                .currentBackStackEntryAsState().value?.destination?.route

            val showBottomBar = navItemList.any { it.route == currentDestination }


            val viewModel = koinViewModel<MangaViewModel>()
            Scaffold(
                modifier = Modifier
                    //.fillMaxSize()
                //.height(500.dp)
                ,
                containerColor = Color.Transparent,
                bottomBar = {
                    if (showBottomBar){
                        NavigationBar (
                            containerColor = Color.Transparent,
                            tonalElevation = 0.dp,
                            modifier = Modifier
                            //    .height(48.dp)
                            ,

                            ) {
                            val currentDestination = navController.currentBackStackEntryAsState().value?.destination

                            navItemList.forEach { navItem ->
                                val selected = currentDestination?.route == navItem.route
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = {
                                        navController.navigate(navItem.route) {
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

                }
            ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding()
                    ) {
                        composable("home") {
                            HomePage(
                                viewModel,
                                modifier = Modifier
                                    .padding(innerPadding),
                                navController
                            )
                        }
                        composable("sources") {
                            SourcePage(
                                modifier = Modifier
                                    .padding(innerPadding),
                                viewModel,
                                navController
                            )
                        }
                        composable("settings") {
                            SettingsPage(

                            )
                        }

                        composable<SourceNavigation> {
                            val source: SourceNavigation = it.toRoute()
                            MangaBat(modifier = Modifier, navController,source.url)
                        }
                        composable<MangaUrl> {
                            val mangaUrl: MangaUrl = it.toRoute()
                            ItemDetail(viewModel, mangaUrl.url, navController, source = mangaUrl.source)
                        }
                        composable<ChapterUrl> {
                            val chapterUrl: ChapterUrl = it.toRoute()
                            ChapterReader(modifier = Modifier,chapterUrl.url, navController,chapterUrl.source)
                        }
                    }


            }
        }
        }

    }