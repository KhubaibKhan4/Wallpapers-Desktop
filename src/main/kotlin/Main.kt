import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.res.ResourceLoader
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
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
    var isOpen by remember {
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
                    shortcut = KeyShortcut(key = Key.R, ctrl = true)
                )
                Item(
                    "Search",
                    onClick = {
                        isSearchActive = !isSearchActive
                    },
                    shortcut = KeyShortcut(key = Key.S, ctrl = true)
                )
                Menu("Actions", mnemonic = 'A') {
                    Item("About", icon = AboutIcon, onClick = { action = "Last action: About" })
                    Item("Exit", onClick = { isOpen = false }, shortcut = KeyShortcut(key = Key.Escape), mnemonic = 'E')
                }
            }

            Menu("Setting", mnemonic = 'S') {
                Menu(
                    "Themes", enabled = true,
                    mnemonic = 'T',
                ) {
                    Item("Dark Theme", enabled = true, mnemonic = 'D',
                        shortcut = KeyShortcut(key = Key.D, ctrl = true),
                        onClick = {
                            isActive = true
                        })
                    Item("Light Theme", enabled = true, mnemonic = 'L',
                        shortcut = KeyShortcut(key = Key.L, ctrl = true),
                        onClick = {
                            isActive = true
                        })
                }
            }

        }
        when (currentScreen) {
            Screen.MAIN -> MainScreen(samplePhoto, isRefresh, isSearchActive) { photo ->
                currentScreen = Screen.DETAIL
                selectedDPhoto = photo
            }

            Screen.DETAIL -> DetailScreen(selectedDPhoto) {
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
                tooltip = "Wallpaper",
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
                        }
                    )
                    Item(
                        text = "Exit",
                        onClick = {
                            exitApplication()
                        }
                    )
                }
            )
        }
        if (isActive) {
            Popup(
                alignment = Alignment.Center,
                offset = IntOffset(x = 0, y = 0),
                focusable = true,
                onDismissRequest = { isVisible = !isVisible },
            ) {
                Column(
                    modifier = Modifier.width(200.dp)
                        .height(140.dp)
                        .background(color = Color.White),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("This is a popup with buttons", style = MaterialTheme.typography.h6)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { isActive = false }) {
                            Text("OK")
                        }
                        Button(onClick = { isVisible = false }) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }
}

object AboutIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color(0xFFFFA500))
    }
}