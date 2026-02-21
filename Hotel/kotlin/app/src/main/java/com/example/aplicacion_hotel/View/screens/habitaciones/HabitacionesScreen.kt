package com.example.aplicacion_hotel.View.screens.habitaciones

<<<<<<< Updated upstream
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
=======
import androidx.compose.foundation.clickable
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
>>>>>>> Stashed changes


@Composable
<<<<<<< Updated upstream
fun HabitacionesScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Habitaciones")
    }
}
//navController.navigate("reservas/${habitacion.id}")
=======
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

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(habitaciones, key = { it._id }) { hab ->
                HabitacionCard(
                    habitacion = hab,
                    onInfo = { habitacionInfo = hab },
                    onSelect = {
                        // AQUÍ ES DONDE SE VINCULA LA NAVEGACIÓN
                        navController.navigate(
                            Routes.Pago.createRoute(hab._id, hab.precionoche)
                        )
                    }
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
    onInfo: () -> Unit,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }, // <--- AHORA TODA LA CARTA ES CLICABLE
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
                Text(text = "Habitación ${habitacion.numero}", style = MaterialTheme.typography.titleMedium)
                Text(text = "${habitacion.precionoche} € / noche")
            }

            IconButton(onClick = onInfo) {
                Icon(Icons.Filled.Info, contentDescription = null)
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
>>>>>>> Stashed changes
