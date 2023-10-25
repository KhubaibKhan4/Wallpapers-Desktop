package utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import java.net.HttpURLConnection
import java.net.URL

object Constant {
    const val AUTHORIZATION = "Authorization"
    const val KEY = "psWUxJSiSZbvIQh0qDy6CcKDS4dsBl7fs3L0dzjcxSxe4kYSxAltOS0I"
}
fun loadPicture(url: String): ImageBitmap? {
    return try {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.setRequestProperty("User-Agent", "Mozilla/5.0")
        val inputStream = connection.inputStream
        val byteArray = inputStream.readBytes()
        Image.makeFromEncoded(byteArray).toComposeImageBitmap()
    } catch (e: Exception) {
        // Handle the exception appropriately, for example, log the error or provide a placeholder image.
        e.printStackTrace()
        null
    }
}