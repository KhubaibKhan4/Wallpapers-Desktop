package ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import components.WallpaperList
import data.model.Photo.Photo
import data.model.Photo.Wallpapers
import data.model.Video.VideoWallpaper
import data.remote.WallpaperApiClient
import io.ktor.client.plugins.*
import kotlinx.coroutines.launch
import java.awt.Cursor
import javax.swing.JToolBar.Separator

@Composable
fun MainScreen(
    samplePhoto: Photo,
    isRefresh: Boolean,
    isSearchActive: Boolean,
    isDarkTheme: Boolean,
    isVideoMode: Boolean,
    onItemClick: (Photo) -> Unit
) {


    val scope = rememberCoroutineScope()
    var data by remember {
        mutableStateOf<Wallpapers?>(null)
    }

    var videoWallpaper by remember {
        mutableStateOf<VideoWallpaper?>(null)
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

    LaunchedEffect(isVideoMode) {
        /* isLoading = true
         try {
             val videoWallpapers = WallpaperApiClient.getPopularVideo(page, 80)
             videoWallpaper = videoWallpapers
             isLoading = false
             println("$videoWallpaper")

         } catch (e: ClientRequestException) {
             e.printStackTrace()
             isLoading = false
         } finally {
             isLoading = false
         }*/

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

    MaterialTheme(colors = if (isDarkTheme) darkColors() else lightColors()) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(color = if (isDarkTheme) Color.DarkGray else Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource("logo.png"),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                            .padding(bottom = 10.dp)
                    )
                    Separator()
                    LinearProgressIndicator(
                        modifier = Modifier.width(
                            40.dp
                        ),
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .background(
                        color = if (isDarkTheme) Color.Black.copy(alpha = 0.80f) else Color.White,
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(isSearchActive) {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier.fillMaxWidth(0.4f).padding(top = 8.dp, bottom = 8.dp)
                            .pointerHoverIcon(icon = PointerIcon(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR))),
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = if (isDarkTheme) Color.White else Color.Black,
                            backgroundColor = Color.LightGray,
                            cursorColor = if (isDarkTheme) Color.White else Color.DarkGray,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            trailingIconColor = if (isDarkTheme) Color.White else Color.DarkGray,
                            placeholderColor = if (isDarkTheme) Color.White else Color.DarkGray,
                        ),
                        enabled = true,
                        placeholder = {
                            Text(text = "Search Wallpapers")
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        isSearch = true
                                        isLoading = true
                                        val searchData = WallpaperApiClient.getSearched(
                                            query = text,
                                            page = 1,
                                            per_page = 80,
                                        )
                                        data = searchData
                                    }
                                }, modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand)
                            ) {
                                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            autoCorrect = true,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(onSearch = {
                            scope.launch {
                                val searchData = WallpaperApiClient.getSearched(
                                    query = text,
                                    page = 1,
                                    per_page = 80,
                                )
                                data = searchData
                            }
                        })

                    )

                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (isVideoMode) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column {

                                Text("Wait, Just starting working on Video Mode.....")
                            }
                        }
                    } else {
                        data?.photos?.let { wallpapers ->
                            WallpaperList(wallpapers, onItemClick, isDarkTheme)
                        }

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
                        modifier = Modifier.size(40.dp)
                            .background(
                                color = if (isDarkTheme) Color.White.copy(alpha = 0.80f) else Color.DarkGray.copy(alpha = 0.80f)
                            )
                            .align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = if (isDarkTheme) Color.Black else Color.White
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
                        modifier = Modifier.size(40.dp)
                            .background(
                                color = if (isDarkTheme) Color.White.copy(alpha = 0.80f) else Color.DarkGray.copy(alpha = 0.80f)
                            )
                            .align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = if (isDarkTheme) Color.Black else Color.White
                        )
                    }
                }

            }
        }

    }
}