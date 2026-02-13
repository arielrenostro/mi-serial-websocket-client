package com.masterinjection.serialwebsocketclient.domain.listener

interface ServerMessageListener {
    fun onMessage(message: ServerMessage)
    fun onError(e: Exception)
}