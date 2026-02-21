package com.example.aplicacion_hotel.View.screens.habitaciones

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.aplicacion_hotel.Model.Habitacion
import com.example.aplicacion_hotel.ViewModel.HabitacionesViewModel
import com.example.aplicacion_hotel.ViewModel.HabitacionesViewModelFactory
import com.example.aplicacion_hotel.View.navigation.Routes
import com.example.aplicacion_hotel.utils.SessionManager

@Composable
fun HabitacionesScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val vm: HabitacionesViewModel = viewModel(
        factory = HabitacionesViewModelFactory(sessionManager)
    )

    val habitaciones by vm.habitaciones
    val cargando by vm.cargando
    val error by vm.error

    var habitacionInfo by remember { mutableStateOf<Habitacion?>(null) }

    // Estados para el margen de fechas
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        vm.cargarHabitaciones(soloDisponibles = true)
    }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("Buscar por fechas", style = MaterialTheme.typography.titleLarge)

        // --- Formulario de Búsqueda ---
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(Modifier.padding(12.dp)) {
                Row(Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = fechaInicio,
                        onValueChange = { fechaInicio = it },
                        label = { Text("Entrada (YYYY-MM-DD)") },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("2024-10-10") }
                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = fechaFin,
                        onValueChange = { fechaFin = it },
                        label = { Text("Salida (YYYY-MM-DD)") },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("2024-10-15") }
                    )
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (fechaInicio.length == 10 && fechaFin.length == 10) {
                            vm.buscarDisponibles(fechaInicio, fechaFin)
                        } else {
                            vm.cargarHabitaciones(true)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Buscar Disponibilidad")
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Text("Habitaciones encontradas", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        if (cargando) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(habitaciones, key = { it._id }) { hab ->
                    HabitacionCard(
                        habitacion = hab,
                        onInfo = { habitacionInfo = hab },
                        onSelect = {
                            // Al seleccionar, vamos a la pantalla de pago
                            navController.navigate(
                                Routes.Pago.createRoute(hab._id, hab.precionoche)
                            )
                        }
                    )
                }
            }
        }
    }

    habitacionInfo?.let { hab ->
        InfoHabitacionDialog(habitacion = hab, onDismiss = { habitacionInfo = null })
    }
}

@Composable
private fun HabitacionCard(
    habitacion: Habitacion,
    onInfo: () -> Unit,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onSelect() },
        shape = RoundedCornerShape(14.dp),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = habitacion.imagen,
                contentDescription = null,
                modifier = Modifier.size(72.dp).clip(RoundedCornerShape(10.dp))
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Habitación ${habitacion.numero} · ${habitacion.tipo}", style = MaterialTheme.typography.titleMedium)
                Text(text = "${habitacion.precionoche} € / noche", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onInfo) {
                Icon(Icons.Filled.Info, contentDescription = "Información")
            }
        }
    }
}

@Composable
private fun InfoHabitacionDialog(habitacion: Habitacion, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onDismiss) { Text("Cerrar") } },
        title = { Text("Habitación ${habitacion.numero} · ${habitacion.tipo}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Precio/noche: ${habitacion.precionoche} €")
                Text("Máx. ocupantes: ${habitacion.max_ocupantes}")
                Text("Descripción: ${habitacion.descripcion}")
            }
        }
    )
}