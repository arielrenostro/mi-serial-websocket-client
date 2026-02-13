package com.masterinjection.serialwebsocketclient.domain.service

import com.masterinjection.serialwebsocketclient.domain.model.SerialPort
import kotlinx.coroutines.runBlocking
import com.fazecast.jSerialComm.SerialPort as JSerialPort

class SerialPortService private constructor() {

    companion object {
        private var instance: SerialPortService? = null

        fun getInstance(): SerialPortService {
            return instance ?: SerialPortService().also { instance = it }
        }
    }

    fun list() = JSerialPort.getCommPorts()
        .map { SerialPort.fromJSerialPort(it) }
        .sortedBy { it.portName }

    fun connect(serialPort: SerialPort) {
        serialPort.socket.setBaudRate(115200)
        if (!serialPort.socket.open()) {
            throw RuntimeException("Não foi possível abrir a porta ${serialPort.portName}")
        }
    }

    fun disconnect(serialPort: SerialPort) {
        serialPort.socket.close()
    }

}