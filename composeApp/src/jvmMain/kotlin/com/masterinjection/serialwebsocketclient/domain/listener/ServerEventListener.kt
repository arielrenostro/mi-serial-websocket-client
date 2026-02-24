package com.masterinjection.serialwebsocketclient.domain.listener

interface ServerEventListener {
    fun onEvent(message: ServerEvent)
    fun onError(e: Exception)
}