package com.masterinjection.serialwebsocketclient.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import com.masterinjection.serialwebsocketclient.viewmodel.TunerViewModel

@Composable
fun TunerSection(vm: TunerViewModel) {
    val state by vm.state.collectAsState()

    Column(
        modifier = Modifier
            .safeContentPadding(),
    ) {
        Text("Clientes")
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.customers) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(item.name)
                }
                Divider()
            }
        }

    }
}