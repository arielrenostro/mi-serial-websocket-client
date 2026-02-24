package com.masterinjection.serialwebsocketclient.viewmodel

import com.masterinjection.serialwebsocketclient.domain.model.ConnectionStatus
import com.masterinjection.serialwebsocketclient.domain.model.Customer
import com.masterinjection.serialwebsocketclient.domain.model.OperationMode
import com.masterinjection.serialwebsocketclient.domain.model.SerialPort
import com.masterinjection.serialwebsocketclient.domain.service.ServerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.time.Duration.Companion.seconds

class TunerViewModel(
    state: TunerViewModelState,
) : BaseViewModel<TunerViewModelState>(
    state = state,
) {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    constructor(port: SerialPort) : this(
        TunerViewModelState(
            port = port,
            customers = listOf(
                Customer(
                    id = UUID.randomUUID().toString(),
                    name = "Fake peugeot",
                    tunerConnected = false
                ),
                Customer(
                    id = UUID.randomUUID().toString(),
                    name = "Fake peugeot connected",
                    tunerConnected = true
                )
            ),
        )
    )

    fun startPolling() {
        scope.launch {
            while (!Thread.interrupted()) {
                delay(2.seconds)
                val serverService = ServerService.getInstance()
                if (serverService.mode != OperationMode.TUNER) {
                    continue
                }
                serverService.requestCustomerList()
            }
        }
    }

    fun stopPolling() {
        scope.cancel()
    }

    fun connectIntoCustomer(customer: Customer) {
        ServerService.getInstance().requestCustomerConnect(customer)
    }
}

data class TunerViewModelState(
    val port: SerialPort,
    val customers: List<Customer>,
)