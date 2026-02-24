package com.masterinjection.serialwebsocketclient.domain.dto.servermessage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PAIR_DISCONNECTED")
class PairDisconnectedMessage(
    val timestamp: Long,
) : ServerMessage(
    type = ServerMessageType.PAIR_DISCONNECTED,
)