package com.masterinjection.serialwebsocketclient.domain.service

import com.masterinjection.serialwebsocketclient.domain.model.Customer
import java.util.*

class TunerService private constructor() {

    companion object {
        private var instance: TunerService? = null

        fun getInstance(): TunerService {
            return instance ?: TunerService().also { instance = it }
        }
    }

    fun listCustomers(): List<Customer> {
        // TODO
        return listOf(
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
        )
    }

}