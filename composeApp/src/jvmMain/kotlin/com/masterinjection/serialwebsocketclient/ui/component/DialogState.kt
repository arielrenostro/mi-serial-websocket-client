package com.masterinjection.serialwebsocketclient.ui.component

sealed class DialogState {
    data class Alert(
        val title: String,
        val message: String,
        val onConfirm: (() -> Unit)? = null
    ) : DialogState()
}