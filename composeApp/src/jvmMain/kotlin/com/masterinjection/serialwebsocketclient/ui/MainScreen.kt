package com.masterinjection.serialwebsocketclient.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.masterinjection.serialwebsocketclient.domain.model.OperationMode
import com.masterinjection.serialwebsocketclient.domain.model.SerialPort
import com.masterinjection.serialwebsocketclient.ui.component.SelectMenu
import com.masterinjection.serialwebsocketclient.viewmodel.MainViewModel
import com.masterinjection.serialwebsocketclient.viewmodel.TunerViewModel

@Composable
fun MainScreen(vm: MainViewModel) {
    MaterialTheme {
        val state by vm.state.collectAsState()

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {
            Text("Modo de Operação")
            Row(
                modifier = Modifier
                    .safeContentPadding(),
                horizontalArrangement = Arrangement.Start,
            ) {
                Box(
                    modifier = Modifier
                        .size(Dp(300.0f), Dp.Unspecified),
                ) {
                    SelectMenu(
                        label = "Modo",
                        enabled = state.operationModeEnabled,
                        options = state.operationModes,
                        selected = state.selectedOperationMode,
                        onSelected = vm::selectOperationMode
                    )
                }
            }

            Spacer(Modifier.height(Dp(10.0f)))

            Text("Comunicação")
            Row(
                modifier = Modifier
                    .safeContentPadding(),
                horizontalArrangement = Arrangement.Start,
            ) {
                Box(
                    modifier = Modifier
                        .size(Dp(300.0f), Dp.Unspecified),
                ) {
                    SelectMenu(
                        label = "Porta",
                        enabled = state.selectPortsEnabled,
                        options = state.availablePorts,
                        optionLabel = { "[${it.portName}] - ${it.description}" },
                        selected = state.selectedPort,
                        onSelected = vm::selectPort
                    )
                }

                Spacer(Modifier.width(Dp(10.0f)))

                IconButton(
                    onClick = { vm.refreshPortList() },
                    enabled = state.selectPortsEnabled,
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Atualizar"
                    )
                }

                Spacer(Modifier.width(Dp(20.0f)))

                Button(
                    enabled = state.connectEnabled,
                    onClick = { vm.connect() },
                ) {
                    Text("Conectar")
                }

                Spacer(Modifier.width(Dp(20.0f)))

                Button(
                    enabled = state.disconnectEnabled,
                    onClick = { vm.disconnect() },
                ) {
                    Text("Desconectar")
                }

                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .safeContentPadding(),
                    horizontalAlignment = Alignment.Start,
                ) {

                }
            }

            Spacer(Modifier.height(Dp(10.0f)))

            if (state.connected) {
                when (state.selectedOperationMode) {
                    OperationMode.CUSTOMER -> {

                    }

                    OperationMode.TUNER -> {
                        TunerSection(
                            vm = TunerViewModel(
                                port = state.selectedPort!!,
                            )
                        )
                    }
                }
            }

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