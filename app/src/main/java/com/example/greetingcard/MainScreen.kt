    package com.example.greetingcard

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.greetingcard.database.MangaViewModel
import com.example.greetingcard.pages.HomePage
import com.example.greetingcard.pages.MySettingsScreen
import com.example.greetingcard.pages.NotificationPage
import com.example.greetingcard.settings.SettingsViewModel
import com.example.greetingcard.settings.SettingsViewModelFactory
import com.example.greetingcard.sources.manganelo.ChapterReader
import com.example.greetingcard.sources.manganelo.ItemDetail
import com.example.greetingcard.sources.manganelo.MangaNelo


    @SuppressLint("RememberReturnType")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
    fun MainScreen(modifier: Modifier = Modifier, mangaViewModel: MangaViewModel) {

        val navController = rememberNavController()

        val navItemList = listOf(
            NavItem("Home", Icons.Default.Home, "home", badgeCount = 0),
            NavItem("Sources", Icons.Default.Notifications, "notifications", badgeCount = 0),
            NavItem("Settings", Icons.Default.Settings, route =  "settings", badgeCount = 0)
        )

        val currentDestination = navController
            .currentBackStackEntryAsState().value?.destination?.route

        val showBottomBar = navItemList.any { it.route == currentDestination }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar =  {
                    if (showBottomBar) {
                        BottomNavBar(
                            navItems = navItemList,
                            currentRoute = currentDestination,
                            onItemSelected = { route ->
                                navController.navigate(route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                }
                            }
                        )
                }
            }
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") { HomePage(navController = navController,mangaViewModel = mangaViewModel) }
                composable("notifications") { NotificationPage(navController = navController) }
                composable("settings") { backStackEntry ->
                    val context = LocalContext.current.applicationContext
                    val settingsViewModel: SettingsViewModel = viewModel(
                        key = "settingsViewModel",
                        factory = SettingsViewModelFactory(context)
                    )

                    MySettingsScreen(viewModel = settingsViewModel)
                }

                composable("itemDetail/{manga_url}"){ navBackStackEntry ->
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
                }
                composable("detail/{source}") { navBackStackEntry ->
                    val source = navBackStackEntry.arguments?.getString("source")
                    MangaNelo(navController =navController)
                }

                composable("itemDetail/{manga_url}"){ navBackStackEntry ->
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
                }
            }
        }
    }

    @Composable
    fun BaseScaffold(
        showBottomBar: Boolean,
        navItems: List<NavItem>,
        currentRoute: String?,
        onItemSelected: (String) -> Unit,
        content: @Composable (PaddingValues) -> Unit
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = if (showBottomBar) {
                {
                    BottomNavBar(
                        navItems = navItems,
                        currentRoute = currentRoute,
                        onItemSelected = onItemSelected
                    )
                }
            } else {
                {}
            },
            content = content
        )
    }

    @Composable

    fun BottomNavBar(
        navItems: List<NavItem>,
        currentRoute: String?,
        onItemSelected: (String) -> Unit
    ) {
        NavigationBar (
            contentColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ){
            navItems.forEach { navItem ->
                NavigationBarItem(
                    selected = currentRoute == navItem.route,
                    onClick = { onItemSelected(navItem.route) },
                    icon = {
                        BadgedBox(badge = {
                            if (navItem.badgeCount > 0) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.error
                                ) {
                                    Text(navItem.badgeCount.toString())
                                }
                            }
                        }) {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = navItem.label,
                                tint = if (currentRoute == navItem.route)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    label = {
                        Text(
                            text = navItem.label,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (currentRoute == navItem.route)
                                MaterialTheme.colorScheme.primary
                            else
                            MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    alwaysShowLabel = true
                )
            }
        }
    }