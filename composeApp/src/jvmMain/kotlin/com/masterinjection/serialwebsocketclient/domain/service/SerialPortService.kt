package com.masterinjection.serialwebsocketclient.domain.service

import com.masterinjection.serialwebsocketclient.domain.listener.SerialEvent
import com.masterinjection.serialwebsocketclient.domain.listener.SerialEventListener
import com.masterinjection.serialwebsocketclient.domain.model.ConnectionStatus
import com.masterinjection.serialwebsocketclient.domain.model.SerialPort
import java.util.*
import com.fazecast.jSerialComm.SerialPort as JSerialPort

/**
 * TODO: Implement a reconnection strategy!
 *  It needs a polling to check connection and a message handler interceptor
 */
class SerialPortService private constructor() {

    companion object {
        private var instance: SerialPortService? = null

        fun getInstance(): SerialPortService {
            return instance ?: SerialPortService().also { instance = it }
        }
    }

    private val listeners = mutableMapOf<String, SerialEventListener>()
    private var openedPort: SerialPort? = null

    fun list() = JSerialPort.getCommPorts()
        .map { SerialPort.fromJSerialPort(it) }
        .sortedBy { it.portName }

    fun connect(serialPort: SerialPort) {
        dispatchListeners {
            it.onEvent(
                SerialEvent.ConnectionEvent(
                    status = ConnectionStatus.CONNECTING,
                    port = serialPort,
                )
            )
        }

        serialPort.socket.setBaudRate(115200)
        if (!serialPort.socket.open()) {
            dispatchListeners {
                it.onEvent(
                    SerialEvent.ConnectionEvent(
                        status = ConnectionStatus.DISCONNECTED,
                        port = serialPort,
                    )
                )
            }
            throw RuntimeException("Não foi possível abrir a porta ${serialPort.portName}")
        }

        openedPort = serialPort

        dispatchListeners {
            it.onEvent(
                SerialEvent.ConnectionEvent(
                    status = ConnectionStatus.CONNECTED,
                    port = serialPort,
                )
            )
        }
    }

    fun disconnect() {
        openedPort?.let { port ->
            openedPort = null
            dispatchListeners {
                it.onEvent(
                    SerialEvent.ConnectionEvent(
                        status = ConnectionStatus.DISCONNECTED,
                        port = port,
                    )
                )
            }
        }
    }

    fun listen(listener: SerialEventListener): String {
        val id = UUID.randomUUID().toString()
        synchronized(listeners) {
            listeners[id] = listener
        }
        return id
    }

    fun unlisten(id: String) {
        synchronized(listeners) {
            listeners.remove(id)
        }
    }

    fun sendToConnectedPort(data: String) {
        openedPort?.socket?.write(data.encodeToByteArray())
    }

    private fun dispatchListeners(fn: (SerialEventListener) -> Unit) {
        synchronized(listeners) {
            listeners.values.forEach {
                try {
                    fn(it)
                } catch (e: Exception) {
                    it.onError(e)
                }
            }
        }
    }

}