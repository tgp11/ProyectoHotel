package com.example.aplicacion_hotel.View.screens.reservas

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacion_hotel.ViewModel.ReservaViewModel
import com.example.aplicacion_hotel.utils.SessionManager

@Composable
fun ReservasScreen() {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val clienteId = remember { sessionManager.getUserId() }

    val viewModel: ReservaViewModel = viewModel()
    val reservas by viewModel.reservas

    var expandedReservaId by remember { mutableStateOf<String?>(null) }
    var reservaIdParaCancelar by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(clienteId) {
        clienteId?.let { viewModel.cargarReservas(it) }
    }

    if (reservaIdParaCancelar != null) {
        AlertDialog(
            onDismissRequest = { reservaIdParaCancelar = null },
            title = { Text("Confirmar cancelación") },
            text = { Text("¿Seguro que quieres cancelar esta reserva?") },
            confirmButton = {
                TextButton(onClick = {
                    clienteId?.let { cid -> viewModel.cancelarReserva(reservaIdParaCancelar!!, cid) }
                    reservaIdParaCancelar = null
                }) { Text("Sí, Cancelar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { reservaIdParaCancelar = null }) { Text("Volver") }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mis Reservas", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        if (reservas.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No tienes reservas aún")
            }
        } else {
            LazyColumn {
                items(reservas) { reserva ->
                    val isExpanded = expandedReservaId == reserva._id
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            .clickable { expandedReservaId = if (isExpanded) null else reserva._id },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Habitación: ${reserva.habitacionId}", style = MaterialTheme.typography.titleMedium)
                            Text("Entrada: ${reserva.fechaEntrada} | Salida: ${reserva.fechaSalida}")
                            
                            AnimatedVisibility(visible = isExpanded) {
                                Column {
                                    Spacer(Modifier.height(8.dp))
                                    Text("Personas: ${reserva.personas}")
                                    Text("Total: ${reserva.precioTotal}€")
                                    Text("Estado: ${if (reserva.cancelacion) "Cancelada" else "Activa"}", 
                                        color = if (reserva.cancelacion) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
                                    
                                    if (!reserva.cancelacion) {
                                        Button(
                                            onClick = { reservaIdParaCancelar = reserva._id },
                                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                        ) { Text("Cancelar Reserva") }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
