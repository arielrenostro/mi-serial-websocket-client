package com.masterinjection.serialwebsocketclient.domain.adapter

import com.masterinjection.serialwebsocketclient.domain.model.SerialPort
import com.fazecast.jSerialComm.SerialPort as JSerialPort

class JSerialPortSocketAdapter(
    private val port: JSerialPort,
) : SerialPort.Socket {

    override fun write(byte: ByteArray): Int = port.writeBytes(byte, byte.size)

    override fun setBaudRate(rate: Int): Boolean = port.setBaudRate(rate)

    override fun open(): Boolean = port.openPort()

    override fun close(): Boolean = port.closePort()

}