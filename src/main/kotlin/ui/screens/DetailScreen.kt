package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import data.model.Photo
import utils.loadPicture


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(selectedDPhoto: Photo, onBackClick: () -> Unit) {
    val uri = selectedDPhoto.url.toString()
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Photo Details") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = {
            Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                loadPicture("${selectedDPhoto.src!!.landscape}")?.let { image ->
                    Image(
                        image,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                            .clip(shape = RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(
                            onClick = { /* Handle setting wallpaper */ },
                            modifier = Modifier
                                .height(50.dp)
                                .width(120.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.LightGray,
                                contentColor = Color.Blue
                            )
                        ) {
                            Text("Set Wallpaper")
                        }
                        TextButton(
                            onClick = {

                            },
                            modifier = Modifier
                                .height(50.dp)
                                .width(120.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.LightGray,
                                contentColor = Color.Blue
                            )
                        ) {
                            Text("Download")
                        }
                        TextButton(
                            onClick = { },
                            modifier = Modifier
                                .height(50.dp)
                                .width(120.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.LightGray,
                                contentColor = Color.Blue
                            )
                        ) {
                            Text("Share")
                        }
                    }
                }
            }
        }
    )
}