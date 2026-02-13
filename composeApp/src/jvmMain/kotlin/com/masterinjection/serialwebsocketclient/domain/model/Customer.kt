package com.masterinjection.serialwebsocketclient.domain.model

data class Customer(
    val id: String,
    val name: String,
    val tunerConnected: Boolean,
)