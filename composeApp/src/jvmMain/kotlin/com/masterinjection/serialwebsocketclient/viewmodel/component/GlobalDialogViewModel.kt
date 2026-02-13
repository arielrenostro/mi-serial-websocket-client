package com.masterinjection.serialwebsocketclient.viewmodel.component

import com.masterinjection.serialwebsocketclient.ui.component.DialogState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GlobalDialogViewModel {

    companion object {
        private val _dialog = MutableStateFlow<DialogState?>(null)

        fun show(dialog: DialogState) {
            _dialog.value = dialog
        }
    }

    val dialog: StateFlow<DialogState?> = _dialog

    fun dismiss() {
        _dialog.value = null
    }
}