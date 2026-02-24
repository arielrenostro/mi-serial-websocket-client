package com.masterinjection.serialwebsocketclient.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.masterinjection.serialwebsocketclient.domain.model.Customer

@Composable
fun CustomersTable(
    data: List<Customer>,
    onCustomerClick: (Customer) -> Unit,
) {
    val headerColor = Color(0xFF2B2B2B)
    val rowColorEven = Color(0xFF1E1E1E)
    val rowColorOdd = Color(0xFF252525)
    val textColor = Color.White
    val borderColor = Color(0xFF3A3A3A)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor)
    ) {

        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerColor)
                .padding(vertical = 8.dp)
        ) {
            HeaderCell(text = "ID", modifier = Modifier.weight(1f))
            HeaderCell(text = "Name", modifier = Modifier.weight(1f))
            HeaderCell(text = "Tuner Con.", modifier = Modifier.weight(0.5f))
            HeaderCell(text = "Ação", modifier = Modifier.weight(0.6f))
        }

        Divider(color = borderColor)

        // ROWS
        LazyColumn {
            itemsIndexed(data) { index, row ->

                val background =
                    if (index % 2 == 0) rowColorEven else rowColorOdd

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(background)
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TableCell(text = row.id, color = textColor, modifier = Modifier.weight(1f))
                    TableCell(text = row.name, color = textColor, modifier = Modifier.weight(1f))
                    TableCell(text = "${row.tunerConnected}", color = textColor, modifier = Modifier.weight(0.5f))

                    Box(
                        modifier = Modifier.weight(0.6f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!row.tunerConnected) {
                            IconButton(
                                onClick = { onCustomerClick(row) }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = "Atualizar",
                                    tint = textColor
                                )
                            }
                        }
                    }
                }

                Divider(color = borderColor)
            }
        }
    }
}

@Composable
private fun HeaderCell(
    text: String,
    modifier: Modifier,
) {
    Text(
        text = text,
        color = Color.White,
        modifier = modifier
            .padding(horizontal = 8.dp),
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun TableCell(
    text: String,
    color: Color,
    modifier: Modifier,
) {
    Text(
        text = text,
        color = color,
        modifier = modifier
            .padding(horizontal = 8.dp)
    )
}