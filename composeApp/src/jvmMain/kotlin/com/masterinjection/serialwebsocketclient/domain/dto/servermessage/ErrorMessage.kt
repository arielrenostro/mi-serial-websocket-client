package com.masterinjection.serialwebsocketclient.domain.dto.servermessage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ERROR")
class ErrorMessage(
    val timestamp: Long,
    val responseTo: Long?,
    val message: String,
) : ServerMessage(
    type = ServerMessageType.ERROR,
)