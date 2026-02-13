package com.masterinjection.serialwebsocketclient

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.masterinjection.serialwebsocketclient.ui.MainScreen
import com.masterinjection.serialwebsocketclient.viewmodel.MainViewModel

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Serial WebSocket Client",
    ) {
        App()
    }
}