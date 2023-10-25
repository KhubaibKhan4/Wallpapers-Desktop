package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import components.WallpaperList
import data.WallpaperApiClient
import data.model.Wallpapers
import io.ktor.client.plugins.*

@Composable
fun MainScreen() {

    var data by remember {
        mutableStateOf<Wallpapers?>(null)
    }

    val isRefreshed by remember {
        mutableStateOf(false)
    }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(isRefreshed) {
        try {
            val wallpapers = WallpaperApiClient.getWallpapers(1, 80)
            data = wallpapers
            isLoading = false
            println("$data")

        } catch (e: ClientRequestException) {
            e.printStackTrace()
            isLoading = false
        }

    }
    MaterialTheme {
        Scaffold(modifier = Modifier.fillMaxWidth().background(color = Color.White), topBar = {
            TopAppBar(
                title = { Text("Wallpaper Desktop") },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Search, null)
                    }
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Refresh, null)
                    }
                },
                backgroundColor = Color.White,
                contentColor = Color.Black
            )
        }
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = it.calculateTopPadding()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    data?.photos?.let { WallpaperList(it) }

                }
            }
        }

    }

}