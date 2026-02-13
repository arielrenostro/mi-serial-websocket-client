package com.masterinjection.serialwebsocketclient.domain.model

import com.masterinjection.serialwebsocketclient.domain.adapter.JSerialPortSocketAdapter
import com.fazecast.jSerialComm.SerialPort as JSerialPort

data class SerialPort(
    val description: String,
    val portName: String,
    val socket: Socket,
) {
    companion object {
        fun fromJSerialPort(serial: JSerialPort) = SerialPort(
            description = serial.portDescription,
            portName = serial.systemPortName,
            socket = JSerialPortSocketAdapter(serial),
        )
    }

    interface Socket {
        fun write(byte: ByteArray): Int
        fun setBaudRate(rate: Int): Boolean
        fun open(): Boolean
        fun close(): Boolean
    }
}

