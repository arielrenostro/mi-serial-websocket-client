package com.masterinjection.serialwebsocketclient.viewmodel

import com.masterinjection.serialwebsocketclient.domain.model.Customer
import com.masterinjection.serialwebsocketclient.domain.model.SerialPort
import com.masterinjection.serialwebsocketclient.domain.service.TunerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TunerViewModel(
    private val port: SerialPort,
) {

    private val tunerService = TunerService.getInstance()

    private val _state = MutableStateFlow(
        State(
            port = port,
            customers = tunerService.listCustomers(),
        )
    )
    val state: StateFlow<State> = _state

    data class State(
        val port: SerialPort,
        val customers: List<Customer>,
    )
}