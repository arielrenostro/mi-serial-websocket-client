package com.masterinjection.serialwebsocketclient.domain.dto.servermessage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("LIST_CUSTOMERS")
class ListCustomersRequestMessage(
    val timestamp: Long,
) : ServerMessage(
    type = ServerMessageType.LIST_CUSTOMERS,
)
