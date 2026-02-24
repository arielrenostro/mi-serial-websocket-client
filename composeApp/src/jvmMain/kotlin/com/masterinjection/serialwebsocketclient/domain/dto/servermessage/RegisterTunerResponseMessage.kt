package com.masterinjection.serialwebsocketclient.domain.dto.servermessage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("REGISTER_TUNER_RESPONSE")
class RegisterTunerResponseMessage(
    val timestamp: Long,
    val responseTo: Long,
    val success: Boolean,
) : ServerMessage(
    type = ServerMessageType.REGISTER_TUNER_RESPONSE,
)