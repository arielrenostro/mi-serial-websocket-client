package com.masterinjection.serialwebsocketclient.ui.conndialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.masterinjection.serialwebsocketclient.ui.component.Direction
import com.masterinjection.serialwebsocketclient.ui.component.RadioMenu
import com.masterinjection.serialwebsocketclient.ui.extension.Spacing
import com.masterinjection.serialwebsocketclient.ui.extension.smPadding
import com.masterinjection.serialwebsocketclient.viewmodel.conndialog.WebSocketConnDialogViewModel


@Composable
fun WebSocketConnDialog(
    vm: WebSocketConnDialogViewModel,
) {
    val state by vm.state.collectAsState()

    Dialog(
        onDismissRequest = { vm.onDismissRequest() },
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier.smPadding(),
            ) {
                RadioMenu(
                    label = "Modo",
                    options = state.modes,
                    optionLabel = { it.toString().let { str -> str[0].uppercaseChar() + str.substring(1).lowercase() } },
                    selected = state.mode,
                    onSelected = vm::selectOperationMode,
                    direction = Direction.Row,
                )

                TextField(
                    value = state.name,
                    onValueChange = vm::onNameChange,
                    maxLines = 1,
                    singleLine = true,
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(Spacing.sm))

                Row(
                    horizontalArrangement = Arrangement.Start,
                ) {
                    TextField(
                        value = state.host,
                        onValueChange = vm::onHostChange,
                        maxLines = 1,
                        singleLine = true,
                        label = { Text("Host") },
                    )

                    Spacer(Modifier.width(Spacing.sm))

                    TextField(
                        value = state.port,
                        onValueChange = vm::onPortChange,
                        maxLines = 1,
                        singleLine = true,
                        label = { Text("Port") },
                    )
                }

                Button(
                    onClick = vm::connect,
                    enabled = state.name.isNotBlank() && state.host.isNotBlank() && state.port.isNotBlank(),
                ) {
                    Text("Conectar")
                }
            }
        }
    }
}