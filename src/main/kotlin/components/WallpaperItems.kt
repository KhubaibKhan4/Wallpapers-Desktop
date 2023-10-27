package components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import data.model.Photo
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.loadPicture
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WallpaperList(photo: List<Photo?>, onItemClick: (Photo) -> Unit) {
    val scope = rememberCoroutineScope()
    val state = rememberLazyGridState(initialFirstVisibleItemIndex = 0, initialFirstVisibleItemScrollOffset = 2)

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 350.dp),
        state = state,
        modifier = Modifier.fillMaxWidth()
            .focusable()
            .onPreviewKeyEvent {
                when {
                    (it.isCtrlPressed && it.key == Key.DirectionUp && it.type == KeyEventType.KeyDown) -> {
                        if (state.firstVisibleItemIndex > 0) {
                            scope.launch {
                                state.animateScrollToItem(state.firstVisibleItemIndex - 1)
                            }
                        }
                        true
                    }
                    (it.isCtrlPressed && it.key == Key.DirectionDown && it.type == KeyEventType.KeyDown) -> {
                        if (state.firstVisibleItemIndex < photo.size - 1) {
                            scope.launch {
                                state.animateScrollToItem(state.firstVisibleItemIndex + 1)
                            }
                        }
                        true
                    }
                    else -> false
                }
            }
    ) {
        items(photo) { photos ->
            photos?.let { WallpaperItems(it, onItemClick) }
        }
    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WallpaperItems(photo: Photo, onItemClick: (Photo) -> Unit) {

    Card(
        elevation = 8.dp,
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .clickable {
                onItemClick(photo)
            }
            .padding(16.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium)
        ) {
            // Load image from the URL
            loadPicture("${photo.src!!.landscape}")?.let { image ->
                Image(
                    image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Color.Black.copy(alpha = 0.4f)
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "By ${photo.photographer}",
                            style = MaterialTheme.typography.subtitle1,
                            color = Color.White
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}