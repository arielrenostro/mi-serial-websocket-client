package com.masterinjection.serialwebsocketclient.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.masterinjection.serialwebsocketclient.ui.extension.smPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> RadioMenu(
    label: String,
    enabled: Boolean = true,
    options: List<T>,
    optionLabel: (T) -> String = { it.toString() },
    selected: T?,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    direction: Direction = Direction.Column,
) {
    Column(
        modifier = modifier.smPadding(),
    ) {
        Text(label, style = MaterialTheme.typography.headlineSmall)

        if (direction == Direction.Column) {
            options.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = selected == item,
                        onClick = { onSelected(item) },
                        enabled = enabled,
                    )
                    Text(optionLabel(item))
                }
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                options.forEach { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = selected == item,
                            onClick = { onSelected(item) },
                            enabled = enabled,
                        )
                        Text(optionLabel(item))
                    }
                }
            }
        }
    }
}

enum class Direction {
    Column, Row
}