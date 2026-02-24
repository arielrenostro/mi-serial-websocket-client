package com.masterinjection.serialwebsocketclient.domain.listener

import com.masterinjection.serialwebsocketclient.domain.model.ConnectionStatus
import com.masterinjection.serialwebsocketclient.domain.model.SerialPort

sealed class SerialEvent {

    data class ConnectionEvent(val status: ConnectionStatus, val port: SerialPort) : SerialEvent()
    data class DataEvent(val data: ByteArray) : SerialEvent() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other is DataEvent && data.contentEquals(other.data)) return true
            return false
        }

        override fun hashCode(): Int {
            return data.contentHashCode()
        }
    }
}