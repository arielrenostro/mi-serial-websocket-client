package com.masterinjection.serialwebsocketclient.data

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*

class WebSocketClient(
    private val onMessage: (WebSocketClient, String) -> Unit,
    private val onConnect: (WebSocketClient) -> Unit,
    private val onClose: (WebSocketClient, reason: CloseReason?) -> Unit,
) {

    private val client = HttpClient(CIO) {
        install(WebSockets)
        engine {
            requestTimeout = 5000
        }
    }
    var description: String = ""
        private set(value) = run { field = value }
    private var session: WebSocketSession? = null

    suspend fun connect(
        host: String,
        port: Int,
        path: String,
    ) {
        val sb = StringBuilder()
        return client.webSocket(
            host = host,
            port = port,
            path = path,
        ) {
            session = this
            description = "$host:$port"

            onConnect(this@WebSocketClient)

            for (frame in incoming) {
                if (frame is Frame.Text) {
                    sb.append(frame.readText())
                    if (frame.fin) {
                        onMessage(this@WebSocketClient, sb.toString())
                        sb.clear()
                    }
                } else if (frame is Frame.Close) {
                    onClose(this@WebSocketClient, frame.readReason())
                }
            }

            onClose(this@WebSocketClient, CloseReason(CloseReason.Codes.INTERNAL_ERROR, ""))
        }
    }

    suspend fun disconnect() {
        val reason = CloseReason(
            CloseReason.Codes.NORMAL,
            "Client disconnect"
        )
        session?.close(reason)
        session = null
        description = ""
        onClose(this@WebSocketClient, reason)
    }

    suspend fun send(encoded: String) {
        session?.send(Frame.Text(encoded))
    }
}