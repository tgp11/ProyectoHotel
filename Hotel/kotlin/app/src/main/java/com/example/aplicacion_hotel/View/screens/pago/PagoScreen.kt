package com.example.aplicacion_hotel.View.screens.pago

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aplicacion_hotel.ViewModel.ReservaViewModel
import com.example.aplicacion_hotel.utils.SessionManager
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PagoScreen(
    navController: NavController,
    habitacionId: String,
    precioNoche: Double
) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val clienteId = remember { sessionManager.getUserId() }

    val viewModel: ReservaViewModel = viewModel()
    val reservaExitosa by viewModel.reservaExitosa

    var fechaEntrada by remember { mutableStateOf("") }
    var fechaSalida by remember { mutableStateOf("") }
    var personas by remember { mutableStateOf("1") }

    var nombreTarjeta by remember { mutableStateOf("") }
    var numeroTarjeta by remember { mutableStateOf("") }
    var fechaVencimiento by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    // Usamos derivedStateOf para que el precio se calcule automáticamente al cambiar las fechas
    val precioTotal by remember(fechaEntrada, fechaSalida, precioNoche) {
        derivedStateOf {
            calcularPrecioTotal(fechaEntrada, fechaSalida, precioNoche)
        }
    }

    LaunchedEffect(reservaExitosa) {
        if (reservaExitosa == true) {
            Toast.makeText(context, "Reserva realizada con éxito", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        } else if (reservaExitosa == false) {
            Toast.makeText(context, "Error al realizar la reserva", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Finalizar Reserva", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Datos de la estancia", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = fechaEntrada,
                    onValueChange = { fechaEntrada = it },
                    label = { Text("Fecha Entrada (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ej: 2023-10-10") }
                )
                OutlinedTextField(
                    value = fechaSalida,
                    onValueChange = { fechaSalida = it },
                    label = { Text("Fecha Salida (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ej: 2023-10-12") }
                )
                OutlinedTextField(
                    value = personas,
                    onValueChange = { personas = it },
                    label = { Text("Número de personas") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Método de Pago", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = nombreTarjeta,
                    onValueChange = { nombreTarjeta = it },
                    label = { Text("Nombre en la tarjeta") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = numeroTarjeta,
                    onValueChange = { if (it.length <= 16) numeroTarjeta = it },
                    label = { Text("Número de tarjeta") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = fechaVencimiento,
                        onValueChange = { fechaVencimiento = it },
                        label = { Text("MM/YY") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { if (it.length <= 3) cvv = it },
                        label = { Text("CVV") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        if (precioTotal > 0) {
            Text(
                text = "Total a pagar: ${String.format(java.util.Locale.US, "%.2f", precioTotal)} €",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (validarCampos(fechaEntrada, fechaSalida, personas, numeroTarjeta, cvv, context)) {
                    clienteId?.let { id ->
                        viewModel.crearReserva(
                            clienteId = id,
                            habitacionId = habitacionId,
                            fechaEntrada = fechaEntrada,
                            fechaSalida = fechaSalida,
                            personas = personas.toIntOrNull() ?: 1,
                            precioTotal = precioTotal
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = precioTotal > 0
        ) {
            Text("Pagar y Reservar")
        }
    }
}

private fun calcularPrecioTotal(entrada: String, salida: String, precioNoche: Double): Double {
    if (entrada.length < 10 || salida.length < 10) return 0.0
    val sdf = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    sdf.isLenient = false
    return try {
        val date1 = sdf.parse(entrada)
        val date2 = sdf.parse(salida)
        if (date1 != null && date2 != null) {
            val diffInMillies = date2.time - date1.time
            if (diffInMillies < 0) return 0.0

            // Calculamos días naturales (si entra el 10 y sale el 11, son 2 días)
            val noches = (diffInMillies / (1000 * 60 * 60 * 24)).toInt()
            val dias = noches + 1

            dias * precioNoche
        } else {
            0.0
        }
    } catch (e: Exception) {
        0.0
    }
}

private fun validarCampos(entrada: String, salida: String, personas: String, tarjeta: String, cvv: String, context: android.content.Context): Boolean {
    if (entrada.isEmpty() || salida.isEmpty() || personas.isEmpty()) {
        Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
        return false
    }

    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        sdf.isLenient = false
        val d1 = sdf.parse(entrada)
        val d2 = sdf.parse(salida)
        if (d1 == null || d2 == null || d2.before(d1)) {
            Toast.makeText(context, "La fecha de salida debe ser posterior a la de entrada", Toast.LENGTH_SHORT).show()
            return false
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Formato de fecha incorrecto (YYYY-MM-DD)", Toast.LENGTH_SHORT).show()
        return false
    }

    if (tarjeta.length < 16 || cvv.length < 3) {
        Toast.makeText(context, "Datos de tarjeta incompletos", Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}