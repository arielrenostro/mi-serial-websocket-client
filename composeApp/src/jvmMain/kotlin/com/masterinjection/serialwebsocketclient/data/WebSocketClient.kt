package com.masterinjection.serialwebsocketclient.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText

class WebSocketClient {

    suspend fun connect(
        host: String,
        port: Int,
        path: String,
        onMessage: (String) -> Unit
    ) {
        val client = HttpClient(CIO) {
            install(WebSockets)
        }

        val sb = StringBuilder()
        return client.webSocket(
            host = host,
            port = port,
            path = path,
        ) {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    sb.append(frame.readText())
                    if (frame.fin) {
                        onMessage(sb.toString())
                        sb.clear()
                    }
                }
            }
        }
    }
}