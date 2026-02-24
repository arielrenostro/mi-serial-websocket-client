package com.masterinjection.serialwebsocketclient.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.masterinjection.serialwebsocketclient.domain.model.ConnectionStatus
import com.masterinjection.serialwebsocketclient.domain.model.SerialPort
import com.masterinjection.serialwebsocketclient.ui.component.ConnectButton
import com.masterinjection.serialwebsocketclient.ui.component.ConnectionStatus
import com.masterinjection.serialwebsocketclient.ui.conndialog.SerialConnDialog
import com.masterinjection.serialwebsocketclient.ui.conndialog.WebSocketConnDialog
import com.masterinjection.serialwebsocketclient.ui.extension.smPadding
import com.masterinjection.serialwebsocketclient.viewmodel.MainViewModel
import com.masterinjection.serialwebsocketclient.viewmodel.TunerViewModel
import com.masterinjection.serialwebsocketclient.viewmodel.conndialog.SerialConnDialogViewModel
import com.masterinjection.serialwebsocketclient.viewmodel.conndialog.WebSocketConnDialogViewModel


@Composable
fun MainScreen() {
    val vm = remember { MainViewModel() }
    val wsConnDialogVm = remember { WebSocketConnDialogViewModel(onDismissRequest = vm::onDialogClose) }
    val serialConnDialogVm = remember { SerialConnDialogViewModel(onDismissRequest = vm::onDialogClose) }

    MaterialTheme {
        val state by vm.state.collectAsState()

        when (state.modalOpen) {
            "websocket" -> {
                WebSocketConnDialog(vm = wsConnDialogVm)
            }

            "serial" -> {
                SerialConnDialog(vm = serialConnDialogVm)
            }
        }

        DisposableEffect(Unit) {
            vm.startPolling()
            onDispose {
                vm.stopPolling()
            }
        }

        Column(
            modifier = Modifier
                .smPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier.smPadding()
                ) {
                    Text("Comunicação Serial")
                    ConnectionStatus(status = state.serialStatus)
                    Text(if (state.serialStatus == ConnectionStatus.CONNECTED) state.serialConnDescription else "")
                    ConnectButton(
                        status = state.serialStatus,
                        onConnectClick = vm::onSerialConnectClick,
                        onDisconnectClick = vm::onSerialDisconnectClick,
                    )
                }

                Column(
                    modifier = Modifier.smPadding()
                ) {
                    Text("Comunicação Servidor")
                    ConnectionStatus(status = state.serverStatus)
                    Text(if (state.serverStatus == ConnectionStatus.CONNECTED) state.serverConnDescription else "")
                    ConnectButton(
                        status = state.serverStatus,
                        onConnectClick = vm::onServerConnectClick,
                        onDisconnectClick = vm::onServerDisconnectClick,
                    )
                }

                state.serverState?.let {
                    Column(
                        modifier = Modifier.smPadding()
                    ) {
                        Text("Status")
                        Text("Modo: ${state.mode?.toString()?.let { m -> m[0].uppercase() + m.substring(1).lowercase() }}")
                        Text("ID: ${it.id}")
                        Text("Nome: ${it.name}")
                        Row {
                            Text("Par: ")
                            ConnectionStatus(status = if (it.connected) ConnectionStatus.CONNECTED else ConnectionStatus.DISCONNECTED)
                        }
                    }
                }
            }

            Spacer(Modifier.height(Dp(10.0f)))

//            if () {
//                when (state.selectedOperationMode) {
//                    OperationMode.CUSTOMER -> {
//
//                    }
//
//                    OperationMode.TUNER -> {
//                        TunerSection(
//                            vm = TunerViewModel(
//                                port = state.selectedPort!!,
//                            )
//                        )
//                    }
//                }
//            }

            TunerSection(
                vm = TunerViewModel(
                    port = SerialPort(
                        description = "Fake",
                        "Fake",
                        socket = object : SerialPort.Socket {
                            override fun write(byte: ByteArray): Int {
                                return byte.size
                            }

                            override fun setBaudRate(rate: Int): Boolean {
                                return true
                            }

                            override fun open(): Boolean {
                                return true
                            }

                            override fun close(): Boolean {
                                return true
                            }
                        }
                    )
                )
            )
        }

    }
}
