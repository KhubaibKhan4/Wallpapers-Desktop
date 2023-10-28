import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.res.ResourceLoader
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.*
import data.model.Photo
import ui.navigation.Screen
import ui.screens.DetailScreen
import ui.screens.MainScreen


@OptIn(ExperimentalComposeUiApi::class)
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
    var isVisible by remember { mutableStateOf(true) }
    var isActive by remember { mutableStateOf(false) }

    var isDarkTheme by remember {
        mutableStateOf(false)
    }

    var isRefresh by remember {
        mutableStateOf(false)
    }
    var isSearchActive by remember {
        mutableStateOf(false)
    }
    var action by remember {
        mutableStateOf("Last Action: None")
    }
    val view = rememberVectorPainter(image = Icons.Default.Accessibility)
    val update = rememberVectorPainter(image = Icons.Default.Update)
    val exit = rememberVectorPainter(image = Icons.Default.ExitToApp)
    val lightTheme = rememberVectorPainter(image = Icons.Default.LightMode)
    val darkTheme = rememberVectorPainter(image = Icons.Default.DarkMode)
    val refresh = rememberVectorPainter(image = Icons.Default.Refresh)
    val search = rememberVectorPainter(image = Icons.Default.Search)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Wallpaper Desktop",
        icon = painterResource("logo.png")
    ) {
        MenuBar {
            Menu(text = "File", mnemonic = 'F') {
                Item(
                    "Refresh",
                    onClick = {
                        isRefresh = !isRefresh
                    },
                    shortcut = KeyShortcut(key = Key.R, ctrl = true),
                    icon = refresh
                )
                Item(
                    "Search",
                    onClick = {
                        isSearchActive = !isSearchActive
                    },
                    shortcut = KeyShortcut(key = Key.S, ctrl = true),
                    icon = search
                )
                Item(
                    "Exit",
                    onClick = { exitApplication() },
                    shortcut = KeyShortcut(key = Key.Escape, alt = true),
                    mnemonic = 'E',
                    icon = exit
                )

            }

            Menu("Setting", mnemonic = 'S') {
                Menu(
                    "Themes", enabled = true,
                    mnemonic = 'T',
                ) {
                    Item(
                        "Dark Theme",
                        enabled = if(isDarkTheme) false else true,
                        mnemonic = 'D',
                        shortcut = KeyShortcut(key = Key.D, ctrl = true),
                        onClick = {
                            isDarkTheme = !isDarkTheme
                        },
                        icon = darkTheme
                    )
                    Item(
                        "Light Theme",
                        enabled = isDarkTheme,
                        mnemonic = 'L',
                        shortcut = KeyShortcut(key = Key.L, ctrl = true),
                        onClick = {
                            isDarkTheme = false
                        },
                        icon = lightTheme
                    )
                }
            }

            Menu("View", mnemonic = 'V') {
                Item(
                    "About",
                    icon = view,
                    onClick = { action = "Last action: About" },
                    shortcut = KeyShortcut(key = Key.A, ctrl = true)
                )

                Item(
                    "Checks for Updates",
                    icon = update,
                    onClick = { action = "Last action: Update" },
                    shortcut = KeyShortcut(key = Key.U, ctrl = true)
                )

            }

        }
        when (currentScreen) {
            Screen.MAIN -> MainScreen(samplePhoto, isRefresh, isSearchActive, isDarkTheme) { photo ->
                currentScreen = Screen.DETAIL
                selectedDPhoto = photo
            }

            Screen.DETAIL -> DetailScreen(selectedDPhoto, isDarkTheme) {
                currentScreen = Screen.MAIN
            }
        }
        if (isVisible) {
            val trayState = rememberTrayState()
            val notification = rememberNotification(
                "Wallpaper Notification",
                "Here there, New Wallpapers are available now.",
                type = Notification.Type.Info
            )
            Tray(
                icon = painterResource(resourcePath = "logo.png", loader = ResourceLoader.Default),
                state = trayState,
                tooltip = "Wallpaper Desktop",
                onAction = {},
                menu = {
                    Item(
                        text = "Settings",
                        enabled = true,
                        onClick = {
                            isActive = true
                        },
                    )
                    Item(
                        text = "Notification",
                        onClick = {
                            trayState.sendNotification(notification)
                        },
                    )
                    Item(
                        text = "Exit",
                        onClick = {
                            exitApplication()
                        },
                    )
                }
            )
        }
    }
}

object AboutIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color(0xFFFFA500))
    }
}