package com.masterinjection.serialwebsocketclient.viewmodel.conndialog

import com.masterinjection.serialwebsocketclient.domain.model.OperationMode
import com.masterinjection.serialwebsocketclient.domain.service.ServerService
import com.masterinjection.serialwebsocketclient.ui.component.DialogState
import com.masterinjection.serialwebsocketclient.viewmodel.BaseViewModel
import com.masterinjection.serialwebsocketclient.viewmodel.component.GlobalDialogViewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class WebSocketConnDialogViewModel(
    state: WebSocketViewModelState,
    val onDismissRequest: () -> Unit,
) : BaseViewModel<WebSocketViewModelState>(
    state = state,
) {

    constructor(onDismissRequest: () -> Unit) : this(
        state = WebSocketViewModelState(
            mode = OperationMode.CUSTOMER,
            modes = OperationMode.entries,
        ),
        onDismissRequest = onDismissRequest,
    )

    fun connect() {
        try {
            ServerService.getInstance().connect(
                host = state.value.host,
                port = state.value.port.toInt(),
                name = state.value.name,
                mode = state.value.mode,
            )
            onDismissRequest()
        } catch (e: Exception) {
            GlobalDialogViewModel.show(
                DialogState.Alert(
                    title = "Falha ao conectar",
                    message = e.message ?: "Erro desconhecido"
                )
            )
        }
    }

    fun selectOperationMode(operationMode: OperationMode) {
        updateState {
            copy(mode = operationMode)
        }
    }

    fun onHostChange(host: String) {
        updateState {
            copy(host = host)
        }
    }

    fun onPortChange(port: String) {
        updateState {
            copy(port = port)
        }
    }

    fun onNameChange(name: String) {
        updateState {
            copy(name = name)
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
data class WebSocketViewModelState(
    val mode: OperationMode,
    val name: String = Uuid.generateV4().toString(),
//    val host: String = "arielrenostro.ddns.net",
//    val port: String = "58080",
    val host: String = "localhost",
    val port: String = "8080",

    val modes: List<OperationMode>,
    val id: String = "",
)