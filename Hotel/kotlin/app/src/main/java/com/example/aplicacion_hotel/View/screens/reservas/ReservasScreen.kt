package com.example.aplicacion_hotel.View.screens.reservas

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.example.aplicacion_hotel.utils.SessionManager


@Composable
fun ReservasScreen() {

    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val clienteId = sessionManager.getUserId()

    // El error "Unresolved reference: viewModel" a veces se soluciona especificando el tipo explícitamente
    // o asegurándose de que la dependencia 'androidx.lifecycle:lifecycle-viewmodel-compose' esté en el build.gradle
    val viewModel: ReservaViewModel = viewModel()
    val reservas by viewModel.reservas

    LaunchedEffect(Unit) {
        clienteId?.let {
            viewModel.cargarReservas(it)
        }
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
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Habitación: ${reserva.habitacionId}", style = MaterialTheme.typography.titleMedium)
                            Text("Entrada: ${reserva.fechaEntrada}")
                            Text("Salida: ${reserva.fechaSalida}")
                            Text("Personas: ${reserva.personas}")
                            Text("Total: ${reserva.precioTotal}€", color = MaterialTheme.colorScheme.primary)
                            Text(
                                text = if (reserva.cancelacion) "Estado: Cancelada" else "Estado: Activa",
                                color = if (reserva.cancelacion) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
