package ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import data.model.Photo.Photo
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(selectedDPhoto: Photo, isDarkTheme: Boolean, onBackClick: () -> Unit) {
    val uri = selectedDPhoto.url.toString()
    val uriHandler = LocalUriHandler.current


    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = if (isDarkTheme) Color.Black else Color.White).padding(16.dp)
    ) {


        val imageUrl = selectedDPhoto.src?.landscape?.let { asyncPainterResource(data = it) }
        if (imageUrl != null) {
            KamelImage(
                resource = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
                    .clip(shape = RoundedCornerShape(24.dp)),
                onLoading = {
                    CircularProgressIndicator(
                        color = if (isDarkTheme) Color.White else MaterialTheme.colors.primary
                    )
                },
                onFailure = {
                    Image(
                        painter = painterResource("logo.png"),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                animationSpec = tween()
            )
        }
        Image(
            imageVector = Icons.Default.KeyboardArrowLeft,
            colorFilter = ColorFilter.tint(
                color = if (isDarkTheme) Color.LightGray else Color.Blue
            ),
            contentDescription = null,
            modifier = Modifier.align(Alignment.TopStart)
                .padding(top = 10.dp, start = 10.dp)
                .clickable {
                    onBackClick()
                }
        )
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
                    onClick = {
                        setWallpaperFromUrl(selectedDPhoto.src!!.landscape.toString())
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
                    Text("Set Wallpaper")
                }
                TextButton(
                    onClick = {
                        val savePath =
                            "${selectedDPhoto.photographer + selectedDPhoto.width + selectedDPhoto.height}.jpg"
                        downloadImage(selectedDPhoto.src!!.landscape.toString(), savePath)
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
                    onClick = {
                        uriHandler.openUri(uri)
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
                    Text("Share")
                }
            }
        }
    }

}

fun downloadImage(imageUrl: String, fileName: String) {
    try {
        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("User-Agent", "Mozilla/5.0") // Set a valid user-agent header
        val inputStream = BufferedInputStream(connection.inputStream)
        val home = System.getProperty("user.home")
        val downloadDir = File(home, "Downloads")
        val file = File(downloadDir, fileName)
        val outputStream = FileOutputStream(file)
        val dataBuffer = ByteArray(1024)
        var bytesRead: Int
        while (inputStream.read(dataBuffer, 0, 1024).also { bytesRead = it } != -1) {
            outputStream.write(dataBuffer, 0, bytesRead)
        }
        outputStream.close()
        inputStream.close()
        showNotification(title = "Wallpaper Image Downloaded", message = "Image downloaded successfully at ${file.absolutePath}")
        println("Image downloaded successfully at ${file.absolutePath}")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun setWallpaperFromUrl(imageUrl: String) {
    try {
        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("User-Agent", "Mozilla/5.0")
        val imageStream = connection.inputStream
        val tempFile = File.createTempFile("temp_image", ".jpg")
        tempFile.deleteOnExit()
        tempFile.outputStream().use { output ->
            imageStream.use { input ->
                input.copyTo(output)
            }
        }
        showNotification(title = "Wallpaper", message = "Wallpaper set successfully.")
        println("Wallpaper set successfully.")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun showNotification(title: String, message: String) {
    if (SystemTray.isSupported()) {
        val tray = SystemTray.getSystemTray()
        val image = Toolkit.getDefaultToolkit().getImage("logo.png")
        val trayIcon = TrayIcon(image, "Notification")
        trayIcon.setImageAutoSize(true)
        trayIcon.toolTip = "Notification"
        tray.add(trayIcon)

        trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO)
    }
}


