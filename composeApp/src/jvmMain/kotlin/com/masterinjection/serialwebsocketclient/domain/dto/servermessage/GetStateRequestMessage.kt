package com.masterinjection.serialwebsocketclient.domain.dto.servermessage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("GET_STATE")
class GetStateRequestMessage(
    val timestamp: Long,
) : ServerMessage(
    type = ServerMessageType.GET_STATE,
)
