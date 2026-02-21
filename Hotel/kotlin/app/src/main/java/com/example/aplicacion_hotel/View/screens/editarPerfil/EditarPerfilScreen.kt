package com.example.aplicacion_hotel.View.screens.editarPerfil

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.aplicacion_hotel.ViewModel.EditarPerfilViewModel
import com.example.aplicacion_hotel.ViewModel.EditarPerfilViewModelFactory
import com.example.aplicacion_hotel.utils.HotelSessionManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(
    navController: androidx.navigation.NavController
) {

    val context = LocalContext.current
    val session = remember { HotelSessionManager(context) }

    val vm: EditarPerfilViewModel = viewModel(
        factory = EditarPerfilViewModelFactory(session, context)
    )

    // Cargar datos la primera vez
    LaunchedEffect(Unit) { vm.loadFromSession() }

    // Navegar al guardar OK
    LaunchedEffect(vm.success) {
        if (vm.success) {
            navController.popBackStack() // vuelve a Perfil
        }
    }

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        vm.setFoto(uri)
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var expandedSexo by remember { mutableStateOf(false) }
    val sexoTexto = when (vm.sexo) {
        "M" -> "Hombre"
        "F" -> "Mujer"
        "X" -> "Otro"
        else -> ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Editar perfil", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        // Avatar: prioridad -> foto nueva -> foto actual -> icono
        Box(contentAlignment = Alignment.BottomEnd) {

            val baseUrl = "http://192.168.18.18:3000"

            if (vm.fotoNuevaUri != null) {
                AsyncImage(
                    model = vm.fotoNuevaUri,
                    contentDescription = "Foto nueva",
                    modifier = Modifier.size(120.dp).clip(CircleShape)
                )
            } else if (!vm.fotoActualPath.isNullOrBlank()) {
                AsyncImage(
                    model = baseUrl + vm.fotoActualPath,
                    contentDescription = "Foto actual",
                    modifier = Modifier.size(120.dp).clip(CircleShape)
                )
            } else {
                Surface(
                    modifier = Modifier.size(120.dp),
                    shape = CircleShape,
                    tonalElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(64.dp))
                    }
                }
            }

            Button(
                onClick = { pickImage.launch("image/*") },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("Foto")
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = vm.nombre,
            onValueChange = { vm.nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = vm.dni,
            onValueChange = { vm.dni = it },
            label = { Text("DNI") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = vm.email,
            onValueChange = { vm.email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        // Fecha con icono
        OutlinedTextField(
            value = vm.fechaNacimiento,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text("Fecha de nacimiento") },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Sexo dropdown Material3
        ExposedDropdownMenuBox(
            expanded = expandedSexo,
            onExpandedChange = { expandedSexo = !expandedSexo },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = sexoTexto,
                onValueChange = {},
                readOnly = true,
                label = { Text("Sexo") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSexo) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedSexo,
                onDismissRequest = { expandedSexo = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Hombre") },
                    onClick = { vm.sexo = "M"; expandedSexo = false }
                )
                DropdownMenuItem(
                    text = { Text("Mujer") },
                    onClick = { vm.sexo = "F"; expandedSexo = false }
                )
                DropdownMenuItem(
                    text = { Text("Otro") },
                    onClick = { vm.sexo = "X"; expandedSexo = false }
                )
            }
        }

        OutlinedTextField(
            value = vm.ciudad,
            onValueChange = { vm.ciudad = it },
            label = { Text("Ciudad") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // VIP solo lectura (en tu app lo decides tú)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("VIP", modifier = Modifier.weight(1f))
            Switch(
                checked = vm.vip,
                onCheckedChange = null, // readOnly
                enabled = false
            )
        }

        Spacer(Modifier.height(16.dp))

        vm.errorMessage?.let {
            Text(it, color = androidx.compose.ui.graphics.Color.Red)
            Spacer(Modifier.height(8.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { vm.actualizar() },
                enabled = !vm.isLoading,
                modifier = Modifier.weight(1f)
            ) {
                if (vm.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                } else {
                    Text("Guardar")
                }
            }

            OutlinedButton(
                onClick = { navController.popBackStack() },
                enabled = !vm.isLoading,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        vm.fechaNacimiento = formatter.format(Date(millis))
                    }
                    showDatePicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                Button(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}