package com.masterinjection.serialwebsocketclient.domain.dto.servermessage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("REGISTER_TUNER")
class RegisterTunerRequestMessage(
    val timestamp: Long,
    val tuner: Tuner,
) : ServerMessage(
    type = ServerMessageType.REGISTER_TUNER,
) {

    @Serializable
    data class Tuner(
        val id: String,
        val name: String,
    )
}