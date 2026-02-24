package com.masterinjection.serialwebsocketclient.domain.dto.servermessage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("LIST_CUSTOMERS_RESPONSE")
class ListCustomersResponseMessage(
    val timestamp: Long,
    val responseTo: Long,
    val customers: List<CustomerResponse>
) : ServerMessage(
    type = ServerMessageType.LIST_CUSTOMERS_RESPONSE,
) {

    @Serializable
    data class CustomerResponse(
        val id: String,
        val name: String,
        val tunerConnected: Boolean,
    )
}
