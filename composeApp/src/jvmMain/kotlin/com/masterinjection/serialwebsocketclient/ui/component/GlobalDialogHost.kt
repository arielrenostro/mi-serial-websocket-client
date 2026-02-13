package com.masterinjection.serialwebsocketclient.ui.component

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.DialogProperties
import com.masterinjection.serialwebsocketclient.viewmodel.component.GlobalDialogViewModel

@Composable
fun GlobalDialogHost(dialogVm: GlobalDialogViewModel) {

    val dialog by dialogVm.dialog.collectAsState()

    when (val d = dialog) {
        is DialogState.Alert -> {
            AlertDialog(
                onDismissRequest = dialogVm::dismiss,
                title = { Text(d.title) },
                text = { Text(d.message) },
                properties = DialogProperties(
                    dismissOnClickOutside = false,
                ),
                confirmButton = {
                    TextButton(
                        onClick = {
                            d.onConfirm?.invoke()
                            dialogVm.dismiss()
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

        null -> {}
    }
}