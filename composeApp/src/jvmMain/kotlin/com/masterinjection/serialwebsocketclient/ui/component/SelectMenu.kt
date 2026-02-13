package com.masterinjection.serialwebsocketclient.ui.component


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import jdk.jfr.Enabled

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectMenu(
    label: String,
    enabled: Boolean = true,
    options: List<T>,
    optionLabel: (T) -> String = { it.toString() },
    selected: T?,
    onSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (enabled) {
                expanded = !expanded
            }
        }
    ) {
        TextField(
            value = selected?.let { optionLabel(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            maxLines = 1,
            singleLine = true,
            label = { Text(label) },
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            text = optionLabel(it)
                        )
                    },
                    onClick = {
                        onSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}
