package components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import data.model.Photo
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WallpaperList(photo: List<Photo?>, onItemClick: (Photo) -> Unit) {
    val scope = rememberCoroutineScope()
    val state = rememberLazyGridState(initialFirstVisibleItemIndex = 0, initialFirstVisibleItemScrollOffset = 0)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state,
            ),
            interactionSource = MutableInteractionSource()
        )

    }
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun WallpaperItems(photo: Photo, onItemClick: (Photo) -> Unit) {
    var active by remember {
        mutableStateOf(false)
    }
    var number by remember {
        mutableStateOf(0f)
    }
    Card(
        elevation = 8.dp,
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .onPointerEvent(PointerEventType.Scroll) {
                number += it.changes.first().scrollDelta.y
            }
            .onPointerEvent(PointerEventType.Enter) { active = true }
            .onPointerEvent(PointerEventType.Exit) { active = false }
            .clickable {
                onItemClick(photo)
            }
            .padding(16.dp),
        shape = MaterialTheme.shapes.medium,

        ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium)
                .background(color = if (active) Color.LightGray else Color.DarkGray),
        ) {
            TooltipArea(
                tooltip = {
                    Surface(
                        modifier = Modifier.shadow(4.dp),
                        color = Color(255, 255, 210),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Photographer:  ${photo.photographer}",
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                tooltipPlacement = TooltipPlacement.CursorPoint(
                    alignment = Alignment.BottomEnd
                ),
                delayMillis = 600,

                ) {
                val imageUrl = photo.src?.landscape?.let { asyncPainterResource(data = it) }
                if (imageUrl != null) {
                    KamelImage(
                        resource = imageUrl,
                        contentDescription = null,
                        contentScale = if (active) ContentScale.Crop else ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize(),
                        onLoading = { progress ->
                            CircularProgressIndicator(
                                color = Color.White,
                                progress = progress,
                                modifier = Modifier.size(50.dp),
                                strokeWidth = 4.dp,
                                strokeCap = StrokeCap.Round
                            )
                        },
                        onFailure = {
                            Image(
                                painter = painterResource("logo.png"),
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                        },
                        animationSpec = tween(),
                    )
                }

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