package com.masterinjection.serialwebsocketclient.domain.dto.servermessage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("REGISTER_TO_CUSTOMER_RESPONSE")
class RegisterToCustomerResponseMessage(
    val timestamp: Long,
    val responseTo: Long,
    val success: Boolean,
) : ServerMessage(
    type = ServerMessageType.REGISTER_TO_CUSTOMER_RESPONSE,
)
