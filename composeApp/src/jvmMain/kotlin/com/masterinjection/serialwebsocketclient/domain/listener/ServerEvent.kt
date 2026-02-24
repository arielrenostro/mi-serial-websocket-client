package com.masterinjection.serialwebsocketclient.domain.listener

import com.masterinjection.serialwebsocketclient.domain.dto.servermessage.ServerMessage
import com.masterinjection.serialwebsocketclient.domain.model.ConnectionStatus
import com.masterinjection.serialwebsocketclient.domain.model.OperationMode

sealed class ServerEvent {

    data class ConnectionEvent(
        val status: ConnectionStatus,
        val description: String,
        val mode: OperationMode,
    ) : ServerEvent()

    data class MessageEvent(
        val message: ServerMessage,
        val mode: OperationMode,
    ) : ServerEvent()
}