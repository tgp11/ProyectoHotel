package com.example.aplicacion_hotel.View.screens.habitaciones

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.aplicacion_hotel.Model.Habitacion
import com.example.aplicacion_hotel.ViewModel.HabitacionesViewModel
import com.example.aplicacion_hotel.ViewModel.HabitacionesViewModelFactory
import com.example.aplicacion_hotel.utils.SessionManager

@Composable
fun HabitacionesScreen() {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val vm: HabitacionesViewModel = viewModel(
        factory = HabitacionesViewModelFactory(sessionManager)
    )

    val habitaciones by vm.habitaciones
    val cargando by vm.cargando
    val error by vm.error
    val idsCarrito by vm.idsCarrito

    var habitacionInfo by remember { mutableStateOf<Habitacion?>(null) }

    LaunchedEffect(Unit) {
        vm.cargarHabitaciones(soloDisponibles = true)
    }

    Column(Modifier.fillMaxSize().padding(12.dp)) {

        Text("Habitaciones disponibles", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        if (cargando) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Column
        }

        if (error != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(error ?: "Error")
            }
            return@Column
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 12.dp)
        ) {
            items(habitaciones, key = { it._id }) { hab ->
                HabitacionCard(
                    habitacion = hab,
                    enCarrito = idsCarrito.contains(hab._id),
                    onInfo = { habitacionInfo = hab },
                    onToggleCarrito = { vm.toggleCarrito(hab._id) }
                )
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
    enCarrito: Boolean,
    onInfo: () -> Unit,
    onToggleCarrito: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = habitacion.imagen,
                contentDescription = "Imagen habitación",
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Habitación ${habitacion.numero} · ${habitacion.tipo}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${habitacion.precionoche} € / noche",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = onInfo) {
                    Icon(Icons.Filled.Info, contentDescription = "Info")
                }

                IconButton(onClick = onToggleCarrito) {
                    Icon(
                        imageVector = if (enCarrito) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                        contentDescription = "Reservar"
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoHabitacionDialog(
    habitacion: Habitacion,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        },
        title = { Text("Habitación ${habitacion.numero} · ${habitacion.tipo}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Precio/noche: ${habitacion.precionoche} €")
                Text("Máx. ocupantes: ${habitacion.max_ocupantes}")
                Text("Rate: ${habitacion.rate}")
                Text("Oferta: ${if (habitacion.oferta) "Sí" else "No"}")
                Text("Descripción:")
                Text(habitacion.descripcion)
                if (habitacion.servicios.isNotEmpty()) {
                    Text("Servicios:")
                    Text(habitacion.servicios.joinToString(", "))
                }
            }
        }
    )
}