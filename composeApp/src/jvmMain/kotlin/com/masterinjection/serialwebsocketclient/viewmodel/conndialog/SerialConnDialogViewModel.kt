package com.masterinjection.serialwebsocketclient.viewmodel.conndialog

import com.masterinjection.serialwebsocketclient.domain.model.SerialPort
import com.masterinjection.serialwebsocketclient.domain.service.SerialPortService
import com.masterinjection.serialwebsocketclient.ui.component.DialogState
import com.masterinjection.serialwebsocketclient.viewmodel.BaseViewModel
import com.masterinjection.serialwebsocketclient.viewmodel.component.GlobalDialogViewModel

class SerialConnDialogViewModel(
    state: SerialSectionState,
    val onDismissRequest: () -> Unit,
) : BaseViewModel<SerialSectionState>(
    state = state
) {

    constructor(onDismissRequest: () -> Unit) : this(
        state = SerialPortService.getInstance().list().let {
            SerialSectionState(
                availablePorts = it,
                selectedPort = it.firstOrNull(),
            )
        },
        onDismissRequest = onDismissRequest,
    )

    init {
        updateState()
    }

    fun selectPort(it: SerialPort) {
        updateState {
            copy(selectedPort = it)
        }
    }

    fun refreshPortList() {
        val ports = SerialPortService.getInstance().list()

        updateState {
            copy(availablePorts = ports)
        }

        state.value.selectedPort?.let {
            val port = ports
                .find { port -> port.portName == it.portName }
                ?.let { ports.firstOrNull() }
            updateState {
                copy(selectedPort = port)
            }
        }
    }

    fun connect() {
        try {
            SerialPortService.getInstance().connect(state.value.selectedPort!!)
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
}

data class SerialSectionState(
    val availablePorts: List<SerialPort>,
    val selectedPort: SerialPort?,
)