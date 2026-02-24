package com.masterinjection.serialwebsocketclient.domain.dto.servermessage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ECHO_SERIAL_DATA")
class EchoSerialDataMessage(
    val timestamp: Long,
    val data: String,
) : ServerMessage(
    type = ServerMessageType.ECHO_SERIAL_DATA
)
