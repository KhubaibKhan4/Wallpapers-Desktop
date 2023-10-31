import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.res.ResourceLoader
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import data.model.Photo.Photo
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
    val dialogState = rememberDialogState()

    var isDarkTheme by remember {
        mutableStateOf(false)
    }

    var isVideoMode by remember {
        mutableStateOf(false)
    }
    var isAbout by remember {
        mutableStateOf(false)
    }

    var isUpdate by remember {
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
    val video = rememberVectorPainter(image = Icons.Default.FeaturedVideo)


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
                Item(
                    text = "Enable Video Wallpapers",
                    icon = video,
                    enabled = true,
                    onClick = {
                        isVideoMode = !isVideoMode
                    },
                    mnemonic = 'V',
                    shortcut = KeyShortcut(key = Key.V, ctrl = true)
                )
                Menu(
                    "Themes", enabled = true,
                    mnemonic = 'T',
                ) {
                    Item(
                        "Dark Theme",
                        enabled = if (isDarkTheme) false else true,
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
                    onClick = {
                        isAbout = !isAbout
                    },
                    shortcut = KeyShortcut(key = Key.A, ctrl = true)
                )

                Item(
                    "Checks for Updates",
                    icon = update,
                    onClick = {
                        isUpdate = !isUpdate
                    },
                    shortcut = KeyShortcut(key = Key.U, ctrl = true)
                )

            }

        }
        when (currentScreen) {
            Screen.MAIN -> MainScreen(samplePhoto, isRefresh, isSearchActive, isDarkTheme, isVideoMode) { photo ->
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
                        text = if (isDarkTheme) "Light Theme" else "Dark Theme",
                        onClick = {
                            isDarkTheme = !isDarkTheme
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
    if (isAbout) {
        DialogWindow(
            onCloseRequest = {
                isAbout = !isAbout
            },
            state = dialogState,
            visible = true,
            title = "About Us",
            icon = view,
            undecorated = false,
            transparent = false,
            resizable = false,
            enabled = true,
            focusable = true
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "About Us",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Text(
                    text = "Welcome to the Wallpaper Desktop Application, a project crafted with passion and dedication by Muhammad Khubaib Imtiaz. This application is designed to provide you with a stunning collection of wallpapers to beautify your desktop and bring a fresh aesthetic to your workspace. Explore a diverse range of high-quality images carefully curated to cater to your unique preferences and style.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }
        }
    }
    if (isUpdate) {
        DialogWindow(
            onCloseRequest = {
                isUpdate = !isUpdate
            },
            state = dialogState,
            visible = true,
            title = "Update Available",
            icon = update,
            undecorated = false,
            transparent = false,
            resizable = false,
            enabled = true,
            focusable = true
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Update Available",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                Text(
                    text = "A new version of the Wallpaper Desktop Application is available. We recommend updating to the latest version to enjoy new features, enhancements, and bug fixes.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = {
                            isUpdate = !isUpdate
                        },
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        Text("Update Now")
                    }
                    OutlinedButton(
                        onClick = {
                            isUpdate = !isUpdate
                        },
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        Text("Remind Me Later")
                    }
                }
            }
        }
    }
}
