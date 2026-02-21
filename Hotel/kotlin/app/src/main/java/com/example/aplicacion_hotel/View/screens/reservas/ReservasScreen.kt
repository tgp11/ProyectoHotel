package com.example.aplicacion_hotel.View.screens.reservas

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacion_hotel.ViewModel.ReservaViewModel
import com.example.aplicacion_hotel.utils.HotelSessionManager


@Composable
fun ReservasScreen() {
    val context = LocalContext.current
    val sessionManager = HotelSessionManager(context)

    // Obtenemos el ID del usuario que inició sesión
    val clienteId = remember { sessionManager.getUserId() }

    val viewModel: ReservaViewModel = viewModel()
    val reservas by viewModel.reservas

    var expandedReservaId by remember { mutableStateOf<String?>(null) }
    var reservaIdParaCancelar by remember { mutableStateOf<String?>(null) }

    // Solo cargamos reservas si tenemos un ID de usuario válido
    LaunchedEffect(clienteId) {
        clienteId?.let { id ->
            viewModel.cargarReservas(id)
        }
    }

    // Diálogo de confirmación
    if (reservaIdParaCancelar != null) {
        AlertDialog(
            onDismissRequest = { reservaIdParaCancelar = null },
            title = { Text("Confirmar cancelación") },
            text = { Text("¿Seguro que quieres Cancelar la reserva? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        clienteId?.let { cid ->
                            viewModel.cancelarReserva(reservaIdParaCancelar!!, cid)
                        }
                        reservaIdParaCancelar = null
                    }
                ) {
                    Text("Sí, Cancelar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { reservaIdParaCancelar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Mis Reservas", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        if (reservas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No tienes reservas aún")
            }
        } else {
            LazyColumn {
                items(reservas) { reserva ->
                    val isExpanded = expandedReservaId == reserva._id
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                expandedReservaId = if (isExpanded) null else reserva._id
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Habitación: ${reserva.habitacionId}", style = MaterialTheme.typography.titleMedium)
                            Text("Entrada: ${reserva.fechaEntrada}")
                            Text("Salida: ${reserva.fechaSalida}")
                            
                            AnimatedVisibility(visible = isExpanded) {
                                Column {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Personas: ${reserva.personas}")
                                    Text("Total: ${reserva.precioTotal}€", color = MaterialTheme.colorScheme.primary)
                                    Text(
                                        text = if (reserva.cancelacion) "Estado: Cancelada" else "Estado: Activa",
                                        color = if (reserva.cancelacion) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                    )
                                    
                                    if (!reserva.cancelacion) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Button(
                                            onClick = {
                                                reservaIdParaCancelar = reserva._id
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                        ) {
                                            Text("Cancelar Reserva")
                                        }
                                    }
                                }
                            }
                            
                            if (!isExpanded) {
                                Text("Toca para ver más detalles", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                            }
                        }
                    }
                }
            }
        }
    }
}
