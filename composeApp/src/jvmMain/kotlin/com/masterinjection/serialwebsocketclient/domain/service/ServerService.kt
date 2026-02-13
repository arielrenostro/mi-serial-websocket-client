package com.masterinjection.serialwebsocketclient.domain.service

import com.masterinjection.serialwebsocketclient.data.WebSocketClient
import com.masterinjection.serialwebsocketclient.domain.listener.ServerMessageListener
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

class ServerService private constructor() {

    companion object {
        private var instance: ServerService? = null

        fun getInstance(): ServerService {
            return instance ?: ServerService().also { instance = it }
        }
    }

    private val client = WebSocketClient()
    private val listeners = listOf<ServerMessageListener>()

    suspend fun connect(host: String, port: Int, path: String) {
        val json = Json {
            ignoreUnknownKeys = true
        }

        client.connect(host, port, path) { wsMessage ->
            try {
                val message = json.decodeFromString<ServerMessage>(wsMessage)
                dispatchListeners { it.onMessage(message) }
            } catch (e: Exception) {
                dispatchListeners { it.onError(e) }
            }
        }
    }

    private fun dispatchListeners(fn: (ServerMessageListener) -> Unit) {
        listeners.forEach {
            try {
                fn(it)
            } catch (e: Exception) {
                // TODO
            }
        }
    }
}