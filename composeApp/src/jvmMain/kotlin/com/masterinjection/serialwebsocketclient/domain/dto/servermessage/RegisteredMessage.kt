package com.masterinjection.serialwebsocketclient.domain.dto.servermessage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("REGISTERED")
class RegisteredMessage(
    val id: String,
    val name: String,
    val timestamp: Long,
) : ServerMessage(
    type = ServerMessageType.REGISTERED,
)
