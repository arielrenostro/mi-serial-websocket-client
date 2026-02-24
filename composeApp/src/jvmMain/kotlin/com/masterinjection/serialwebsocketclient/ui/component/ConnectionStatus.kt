package com.masterinjection.serialwebsocketclient.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.masterinjection.serialwebsocketclient.domain.model.ConnectionStatus as MConnectionStatus

@Composable
fun ConnectionStatus(
    status: MConnectionStatus,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            when (status) {
                MConnectionStatus.CONNECTING -> "Conectando..."
                MConnectionStatus.CONNECTED -> "Conectado"
                MConnectionStatus.DISCONNECTED -> "Desconectado"
            }
        )

        Spacer(modifier = Modifier.width(4.dp))

        Canvas(
            modifier = Modifier
                .width(16.dp)
                .height(16.dp),
        ) {
            drawCircle(
                color = when (status) {
                    MConnectionStatus.CONNECTING -> Color.Yellow.copy(alpha = 0.8f)
                    MConnectionStatus.CONNECTED -> Color.Green.copy(alpha = 0.8f)
                    MConnectionStatus.DISCONNECTED -> Color.Red.copy(alpha = 0.8f)
                },
                radius = 6.dp.toPx(),
            )
        }
    }
}
