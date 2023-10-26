import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.model.Photo
import ui.navigation.Screen
import ui.screens.DetailScreen
import ui.screens.MainScreen


fun main() = application {

    var currentScreen by remember {
        mutableStateOf(Screen.MAIN)
    }
    val samplePhoto = Photo(
        alt = "Brown Rocks During Golden Hour",
        avgColor = "",
        height = null,
        id = null,
        liked = null,
        photographer = null,
        photographerId = null,
        photographerUrl = null,
        src = null,
        url = null,
        width = null
    )
    var selectedDPhoto by remember {
        mutableStateOf(samplePhoto)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Wallpaper Desktop",
        icon = painterResource("logo.png")
    ) {
        when (currentScreen) {
            Screen.MAIN -> MainScreen(samplePhoto) { photo ->
                currentScreen = Screen.DETAIL
                selectedDPhoto = photo
            }

            Screen.DETAIL -> DetailScreen(selectedDPhoto) {
                currentScreen = Screen.MAIN
            }
        }

    }
}
