package com.masterinjection.serialwebsocketclient.domain.listener

interface SerialEventListener {
    fun onEvent(message: SerialEvent)
    fun onError(e: Exception)
}