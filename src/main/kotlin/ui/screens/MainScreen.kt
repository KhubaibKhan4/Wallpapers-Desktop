package ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import components.WallpaperList
import data.WallpaperApiClient
import data.model.Photo
import data.model.Wallpapers
import io.ktor.client.plugins.*
import kotlinx.coroutines.launch
import java.awt.Cursor

@Composable
fun MainScreen(samplePhoto: Photo, isRefresh: Boolean, isSearchActive: Boolean, onItemClick: (Photo) -> Unit) {

    val scope = rememberCoroutineScope()
    var data by remember {
        mutableStateOf<Wallpapers?>(null)
    }

    var text by remember {
        mutableStateOf("")
    }
    var isActive by remember {
        mutableStateOf(false)
    }
    var isSearch by remember {
        mutableStateOf(false)
    }
    var page by remember {
        mutableStateOf(1)
    }

    var isRefreshed by remember {
        mutableStateOf(false)
    }


    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(isRefresh) {
        isLoading = true
        try {
            val wallpapers = WallpaperApiClient.getWallpapers(page, 80)
            data = wallpapers
            isLoading = false
            println("$data")

        } catch (e: ClientRequestException) {
            e.printStackTrace()
            isLoading = false
        } finally {
            isLoading = false
        }

    }

    if (isSearch) {
        isLoading = true
        scope.launch {
            try {
                val searchedData = WallpaperApiClient.getSearched(text, page = page, per_page = 80)
                data = searchedData
                isLoading = false
                println("Searched:  $searchedData")
            } catch (e: ClientRequestException) {
                isLoading = false
                e.printStackTrace()
            } finally {
                isActive = false // Reset the search variable after the search operation is completed
            }
        }
    }

    MaterialTheme {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(isSearchActive) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier.fillMaxWidth(0.4f),
                        enabled = true,
                        label = {
                            Text("Search")
                        },
                        placeholder = {
                            Text(text = "Search Wallpapers")
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        isSearch = true
                                        isLoading = true
                                        val searchData =
                                            WallpaperApiClient.getSearched(
                                                query = text,
                                                page = 1,
                                                per_page = 80,
                                            )
                                        data = searchData
                                    }
                                },
                                modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand)
                            ) {
                                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                            }
                        }, keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            autoCorrect = true,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(onSearch = {
                            scope.launch {
                                val searchData =
                                    WallpaperApiClient.getSearched(
                                        query = text,
                                        page = 1,
                                        per_page = 80,
                                    )
                                data = searchData
                            }
                        })

                    )
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    data?.photos?.let { wallpapers ->
                        WallpaperList(wallpapers, onItemClick)
                    }
                    IconButton(
                        onClick = {
                            if (!isLoading) {
                                page++
                                isLoading = true
                                scope.launch {
                                    try {
                                        val wallpapers = WallpaperApiClient.getWallpapers(page, 80)
                                        data = wallpapers
                                        isLoading = false
                                        println("$data")
                                    } catch (e: ClientRequestException) {
                                        e.printStackTrace()
                                        isLoading = false
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .background(color = Color.DarkGray.copy(alpha = 0.80f))
                            .align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null,
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = {
                            if (!isLoading && page > 1) {
                                page--
                                isLoading = true
                                scope.launch {
                                    try {
                                        val wallpapers = WallpaperApiClient.getWallpapers(page, 80)
                                        data = wallpapers
                                        isLoading = false
                                        println("$data")
                                    } catch (e: ClientRequestException) {
                                        e.printStackTrace()
                                        isLoading = false
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .background(color = Color.DarkGray.copy(alpha = 0.80f))
                            .align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null,
                            tint = Color.White
                        )
                    }
                }

            }
        }
    }


}