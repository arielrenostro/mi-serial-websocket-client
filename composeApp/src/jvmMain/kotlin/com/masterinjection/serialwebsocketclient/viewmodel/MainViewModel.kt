package com.masterinjection.serialwebsocketclient.viewmodel

import com.masterinjection.serialwebsocketclient.domain.model.OperationMode
import com.masterinjection.serialwebsocketclient.domain.model.SerialPort
import com.masterinjection.serialwebsocketclient.domain.service.SerialPortService
import com.masterinjection.serialwebsocketclient.ui.component.DialogState
import com.masterinjection.serialwebsocketclient.viewmodel.component.GlobalDialogViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel {

    private val serialPortService = SerialPortService.getInstance()

    private val _state = MutableStateFlow(
        State(
            availablePorts = serialPortService.list(),
            selectedPort = serialPortService.list().firstOrNull(),
            operationModes = OperationMode.entries,
            selectedOperationMode = OperationMode.CUSTOMER,
        )
    )
    val state: StateFlow<State> = _state

    init {
        updateState()
    }

    fun selectPort(it: SerialPort) {
        updateState {
            copy(selectedPort = it)
        }
    }

    fun refreshPortList() {
        val ports = serialPortService.list()

        updateState {
            copy(availablePorts = ports)
        }

        _state.value.selectedPort?.let {
            val selectedPortFound = ports.find { port -> port.portName == it.portName }
            if (selectedPortFound != null) {
                updateState {
                    copy(selectedPort = selectedPortFound)
                }
            } else {
                GlobalDialogViewModel.show(
                    DialogState.Alert(
                        title = "Falha na comunicação",
                        message = "Você foi desconectado. Porta conectada não foi encontrada após a atualização."
                    )
                )
                disconnect()
            }
        }
    }

    fun connect() {
        try {
            serialPortService.connect(_state.value.selectedPort!!)
            updateState {
                copy(connected = true)
            }
        } catch (e: Exception) {
            GlobalDialogViewModel.show(
                DialogState.Alert(
                    title = "Falha ao conectar",
                    message = e.message ?: "Erro desconhecido"
                )
            )
        }
    }

    fun disconnect() {
        serialPortService.disconnect(_state.value.selectedPort!!)
        updateState {
            copy(connected = false)
        }
    }

    fun selectOperationMode(operationMode: OperationMode) {
        updateState {
            copy(selectedOperationMode = operationMode)
        }
    }

    private fun updateState() {
        _state.value = _state.value.copy(
            connectEnabled = isConnectButtonEnabled(),
            disconnectEnabled = isDisconnectButtonEnabled(),
            operationModeEnabled = isOperationModeEnabled(),
            selectPortsEnabled = isSelectPortsEnabled(),
        )
    }

    private fun updateState(
        reducer: State.() -> State
    ) {
        _state.value = _state.value.reducer()
        updateState()
    }

    private fun isConnectButtonEnabled(): Boolean =
        _state.value.availablePorts.isNotEmpty()
                && _state.value.selectedPort != null
                && !_state.value.connected

    private fun isDisconnectButtonEnabled(): Boolean =
        _state.value.connected

    private fun isOperationModeEnabled(): Boolean =
        !_state.value.connected

    private fun isSelectPortsEnabled(): Boolean =
        !_state.value.connected

    data class State(
        val availablePorts: List<SerialPort>,
        val selectedPort: SerialPort?,
        val selectPortsEnabled: Boolean = false,

        val operationModes: List<OperationMode>,
        val selectedOperationMode: OperationMode,
        val operationModeEnabled: Boolean = false,

        val connected: Boolean = false,
        val connectEnabled: Boolean = false,
        val disconnectEnabled: Boolean = false,
    )
}