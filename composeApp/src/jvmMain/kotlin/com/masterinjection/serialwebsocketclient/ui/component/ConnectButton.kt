package com.masterinjection.serialwebsocketclient.ui.component

import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.masterinjection.serialwebsocketclient.domain.model.ConnectionStatus

@Composable
fun ConnectButton(
    status: ConnectionStatus,
    onConnectClick: () -> Unit,
    onDisconnectClick: () -> Unit,
) {
    when (status) {
        ConnectionStatus.DISCONNECTED -> {
            Button(onClick = onConnectClick) {
                Text("Conectar")
            }
        }

        ConnectionStatus.CONNECTED,
        ConnectionStatus.CONNECTING -> {
            Button(
                onClick = onDisconnectClick,
                enabled = status == ConnectionStatus.CONNECTED,
            ) {
                Text("Desconectar")
            }
        }
    }
}