package com.masterinjection.serialwebsocketclient.domain.dto.servermessage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("STATE")
class GetStateResponseMessage(
    val timestamp: Long,
    val responseTo: Long,
    val data: Data,
) : ServerMessage(
    type = ServerMessageType.STATE,
) {

    @Serializable
    data class Data(
        val id: String,
        val name: String,
        val connected: Boolean,
    )
}