package com.masterinjection.serialwebsocketclient

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.masterinjection.serialwebsocketclient.domain.service.SerialPortService
import com.masterinjection.serialwebsocketclient.ui.component.GlobalDialogHost
import com.masterinjection.serialwebsocketclient.ui.MainScreen
import com.masterinjection.serialwebsocketclient.viewmodel.component.GlobalDialogViewModel
import com.masterinjection.serialwebsocketclient.viewmodel.MainViewModel

@Composable
@Preview
fun App() {
    val vm = remember { MainViewModel() }
    val dialogVm = remember { GlobalDialogViewModel() }

    Box {
        MainScreen(vm)
        GlobalDialogHost(dialogVm)
    }
}