package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
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
import org.example.project.pages.HomePage
import org.example.project.pages.SourcePage
import org.example.project.pages.SourcePage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import yomu.composeapp.generated.resources.Res
import yomu.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {

        val navItemList = listOf(
            NavItem("Home", Icons.Filled.Home,0),
            NavItem("Sources", Icons.Default.Notifications,0),
            NavItem("Settings", Icons.Default.Settings,0)
        )

        val selectedIndex = remember { mutableStateOf(0) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar {
                    navItemList.forEachIndexed {index, navItem ->
                        NavigationBarItem(
                            selected = selectedIndex.value == index,
                            onClick = {
                                selectedIndex.value = index
                            },
                            icon = {
                                BadgedBox(badge =  {
                                    if(navItem.badgeCount>0)
                                        Badge(){
                                            Text(text = navItem.badgeCount.toString() )
                                        }
                                }) {
                                    Icon(imageVector = navItem.icon, contentDescription = "Icon")
                                }
                            },
                            label = {
                                Text(text = navItem.label)
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            ContentScreen(
                modifier = Modifier
                    .padding(
                        paddingValues = innerPadding
                    ),
                selectedIndex.value
            )
        }

    }
}

@Composable
fun ContentScreen(
    modifier: Modifier,
    selectedIndex:Int,
) {
    when(selectedIndex) {
        0 -> HomePage(modifier = modifier)
        1 -> SourcePage()
        2 -> SourcePage()
    }
}