package com.masterinjection.serialwebsocketclient.viewmodel

import com.masterinjection.serialwebsocketclient.domain.dto.servermessage.ErrorMessage
import com.masterinjection.serialwebsocketclient.domain.dto.servermessage.GetStateResponseMessage
import com.masterinjection.serialwebsocketclient.domain.dto.servermessage.RegisteredMessage
import com.masterinjection.serialwebsocketclient.domain.listener.SerialEvent
import com.masterinjection.serialwebsocketclient.domain.listener.SerialEventListener
import com.masterinjection.serialwebsocketclient.domain.listener.ServerEvent
import com.masterinjection.serialwebsocketclient.domain.listener.ServerEventListener
import com.masterinjection.serialwebsocketclient.domain.model.ConnectionStatus
import com.masterinjection.serialwebsocketclient.domain.model.OperationMode
import com.masterinjection.serialwebsocketclient.domain.service.SerialPortService
import com.masterinjection.serialwebsocketclient.domain.service.ServerService
import com.masterinjection.serialwebsocketclient.ui.component.DialogState
import com.masterinjection.serialwebsocketclient.viewmodel.component.GlobalDialogViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class MainViewModel(
    state: State
) : BaseViewModel<State>(
    state = state
) {
    constructor() : this(State())

    init {
        updateState()
        ServerService.getInstance().listen(ServerListener())
        SerialPortService.getInstance().listen(SerialListener())
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun onDialogClose() {
        updateState {
            copy(modalOpen = "")
        }
    }

    fun onServerConnectClick() {
        updateState {
            copy(modalOpen = "websocket")
        }
    }

    fun onServerDisconnectClick() {
        ServerService.getInstance().disconnect()
    }

    fun onSerialConnectClick() {
        updateState {
            copy(modalOpen = "serial")
        }
    }

    fun onSerialDisconnectClick() {
        SerialPortService.getInstance().disconnect()
    }
//
//    override fun updateDefaultFieldsState(): State {
//        val state = super.updateDefaultFieldsState()
//        return if (state.serverStatus == ConnectionStatus.CONNECTED) {
//            val serverService = ServerService.getInstance()
//            serverService
//                .state
//                ?.let {
//                    state.copy(
//                        mode = serverService.mode,
//                        serverState = it,
//                    )
//                }
//                ?: state.copy(
//                    mode = null,
//                    serverState = null,
//                )
//        } else {
//            state.copy(
//                mode = null,
//                serverState = null,
//            )
//        }
//    }

    private fun showError(msg: String?) {
        GlobalDialogViewModel.show(
            DialogState.Alert(
                title = "Falha no WebSocket",
                message = msg ?: "Erro desconhecido"
            )
        )
    }

    fun startPolling() {
        scope.launch {
            while (!Thread.interrupted()) {
                delay(2.seconds)
                if (state.value.serverStatus != ConnectionStatus.CONNECTED) {
                    continue
                }
                ServerService.getInstance().requestState()
            }
        }
    }

    fun stopPolling() {
        scope.cancel()
    }

    private inner class ServerListener : ServerEventListener {
        override fun onEvent(message: ServerEvent) {
            when (message) {
                is ServerEvent.ConnectionEvent -> updateState {
                    copy(
                        serverStatus = message.status,
                        serverConnDescription = message.description,
                        mode = message.mode,
                        serverState = null,
                    )
                }

                is ServerEvent.MessageEvent -> {
                    when (message.message) {
                        is GetStateResponseMessage -> updateState {
                            copy(
                                serverState = message.message.data,
                                mode = message.mode,
                            )
                        }

                        is RegisteredMessage -> updateState {
                            copy(
                                serverState = GetStateResponseMessage.Data(
                                    id = message.message.id,
                                    name = message.message.name,
                                    connected = false,
                                ),
                                mode = message.mode,
                            )
                        }

                        is ErrorMessage -> {
                            if (state.value.serverStatus == ConnectionStatus.CONNECTING) {
                                showError(message.message.message)
                                updateState { copy(serverStatus = ConnectionStatus.DISCONNECTED) }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }

        override fun onError(e: Exception) {
            if (state.value.serverStatus == ConnectionStatus.CONNECTING) {
                showError(e.message)
                updateState { copy(serverStatus = ConnectionStatus.DISCONNECTED) }
            }
        }
    }

    private inner class SerialListener : SerialEventListener {
        override fun onEvent(message: SerialEvent) {
            when (message) {
                is SerialEvent.ConnectionEvent -> updateState {
                    copy(
                        serialStatus = message.status,
                        serialConnDescription = message.port.description,
                    )
                }

                else -> {}
            }
        }

        override fun onError(e: Exception) {
            if (state.value.serialStatus == ConnectionStatus.CONNECTING) {
                showError(e.message)
                updateState { copy(serialStatus = ConnectionStatus.DISCONNECTED) }
            }
        }
    }
}

data class State(
    val serialStatus: ConnectionStatus = ConnectionStatus.DISCONNECTED,
    val serialConnDescription: String = "",

    val serverStatus: ConnectionStatus = ConnectionStatus.DISCONNECTED,
    val serverConnDescription: String = "",

    val mode: OperationMode? = null,
    val serverState: GetStateResponseMessage.Data? = null,

    val modalOpen: String = "",
)