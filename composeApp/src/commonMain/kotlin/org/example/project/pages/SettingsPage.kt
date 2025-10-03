package org.example.project.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun SettingsPage(

){
    Column {
        Row {

            Text("Dark Mode")

            var checked = remember { mutableStateOf(true) }

            Switch(
                checked = checked.value,
                onCheckedChange = {
                    checked.value = it
                }
            )
        }
    }
}