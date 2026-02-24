package com.masterinjection.serialwebsocketclient.domain.dto.servermessage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("REGISTER_TO_CUSTOMER")
class RegisterToCustomerRequestMessage(
    val timestamp: Long,
    val customerId: String,
) : ServerMessage(
    type = ServerMessageType.REGISTER_TO_CUSTOMER,
)
