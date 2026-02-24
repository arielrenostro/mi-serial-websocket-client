package com.masterinjection.serialwebsocketclient.domain.dto.servermessage

import kotlinx.serialization.Serializable

@Serializable
sealed class ServerMessage(
    val type: ServerMessageType,
)
