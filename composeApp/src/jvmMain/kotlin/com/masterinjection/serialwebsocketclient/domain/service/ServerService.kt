package com.masterinjection.serialwebsocketclient.domain.service

import com.masterinjection.serialwebsocketclient.data.WebSocketClient
import com.masterinjection.serialwebsocketclient.domain.dto.servermessage.*
import com.masterinjection.serialwebsocketclient.domain.listener.ServerEvent
import com.masterinjection.serialwebsocketclient.domain.listener.ServerEventListener
import com.masterinjection.serialwebsocketclient.domain.model.ConnectionStatus
import com.masterinjection.serialwebsocketclient.domain.model.Customer
import com.masterinjection.serialwebsocketclient.domain.model.OperationMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class ServerService private constructor() {

    companion object {
        private var instance: ServerService? = null

        fun getInstance(): ServerService {
            return instance ?: ServerService().also {
                instance = it
                it.listen(it.Listener())
            }
        }
    }

    private val json = Json {
        classDiscriminator = "type"
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private val client = WebSocketClient(
        onConnect = { c ->
            dispatchListeners {
                it.onEvent(
                    ServerEvent.ConnectionEvent(
                        status = ConnectionStatus.CONNECTED,
                        description = c.description,
                        mode = mode!!,
                    )
                )
            }
        },
        onClose = { c, _ ->
            dispatchListeners {
                it.onEvent(
                    ServerEvent.ConnectionEvent(
                        status = ConnectionStatus.DISCONNECTED,
                        description = c.description,
                        mode = mode ?: OperationMode.TUNER,
                    )
                )
            }
        },
        onMessage = { _, wsMessage ->
            try {
                val message = json.decodeFromString<ServerMessage>(wsMessage)
                dispatchListeners {
                    it.onEvent(
                        ServerEvent.MessageEvent(
                            message = message,
                            mode = mode!!,
                        )
                    )
                }
            } catch (e: Exception) {
                dispatchListeners { it.onError(e) }
            }
        }
    )
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val listeners = linkedMapOf<String, ServerEventListener>()

    var mode: OperationMode? = null
        private set
    var state: GetStateResponseMessage.Data? = null
        private set

    fun connect(host: String, port: Int, name: String, mode: OperationMode) {
        scope.launch {
            this@ServerService.mode = mode
            dispatchListeners {
                it.onEvent(
                    ServerEvent.ConnectionEvent(
                        status = ConnectionStatus.CONNECTING,
                        description = client.description,
                        mode = mode,
                    )
                )
            }

            try {
                client.connect(
                    host = host,
                    port = port,
                    path = when (mode) {
                        OperationMode.CUSTOMER -> "/ws/customer?name=${name}"
                        OperationMode.TUNER -> "/ws/tuner?name=${name}"
                    },
                )
            } catch (e: Exception) {
                dispatchListeners { it.onError(e) }
            }
        }
    }

    fun disconnect() {
        scope.launch {
            client.disconnect()
        }
    }

    fun requestState() {
        send(
            GetStateRequestMessage(
                timestamp = System.currentTimeMillis(),
            )
        )
    }

    fun requestCustomerList() {
        send(
            ListCustomersRequestMessage(
                timestamp = System.currentTimeMillis(),
            )
        )
    }

    fun requestCustomerConnect(customer: Customer) {
        send(
            RegisterToCustomerRequestMessage(
                timestamp = System.currentTimeMillis(),
                customerId = customer.id,
            )
        )
    }

    private inline fun <reified T : ServerMessage> send(message: T) {
        scope.launch {
            val encoded = json.encodeToString(message)
            client.send(encoded)
        }
    }

    fun listen(listener: ServerEventListener): String {
        val id = UUID.randomUUID().toString()
        synchronized(listeners) {
            listeners[id] = listener
        }
        return id
    }

    fun unlisten(id: String) {
        synchronized(listeners) {
            listeners.remove(id)
        }
    }

    private fun dispatchListeners(fn: (ServerEventListener) -> Unit) {
        synchronized(listeners) {
            listeners.values.forEach {
                try {
                    fn(it)
                } catch (e: Exception) {
                    it.onError(e)
                }
            }
        }
    }

    private inner class Listener : ServerEventListener {
        override fun onEvent(message: ServerEvent) {
            when (message) {
                is ServerEvent.MessageEvent -> {
                    when (message.message) {
                        is GetStateResponseMessage -> {
                            this@ServerService.state = message.message.data
                        }

                        is RegisteredMessage -> {
                            this@ServerService.state = GetStateResponseMessage.Data(
                                id = message.message.id,
                                name = message.message.name,
                                connected = false,
                            )
                        }

                        else -> {}
                    }
                }

                is ServerEvent.ConnectionEvent -> {
                    when (message.status) {
                        ConnectionStatus.DISCONNECTED -> {
                            mode = null
                            state = null
                        }

                        else -> {}
                    }
                }
            }
        }

        override fun onError(e: Exception) {

        }
    }
}