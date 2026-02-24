package com.masterinjection.serialwebsocketclient.ui.conndialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.masterinjection.serialwebsocketclient.ui.component.SelectMenu
import com.masterinjection.serialwebsocketclient.ui.extension.Spacing
import com.masterinjection.serialwebsocketclient.ui.extension.smPadding
import com.masterinjection.serialwebsocketclient.viewmodel.conndialog.SerialConnDialogViewModel

@Composable
fun SerialConnDialog(
    vm: SerialConnDialogViewModel,
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
                Row(
                    modifier = Modifier.safeContentPadding(),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Box(
                        modifier = Modifier.size(300.dp, Dp.Unspecified),
                    ) {
                        SelectMenu(
                            label = "Porta",
                            options = state.availablePorts,
                            optionLabel = { "[${it.portName}] - ${it.description}" },
                            selected = state.selectedPort,
                            onSelected = vm::selectPort
                        )
                    }

                    Spacer(Modifier.width(Spacing.sm))

                    IconButton(
                        onClick = { vm.refreshPortList() },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Atualizar"
                        )
                    }
                }

                Spacer(Modifier.height(Spacing.sm))

                Button(
                    enabled = state.selectedPort != null,
                    onClick = { vm.connect() },
                ) {
                    Text("Conectar")
                }
            }
        }
    }
}
