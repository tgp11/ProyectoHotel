package com.example.aplicacion_hotel.View.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aplicacion_hotel.View.navigation.Routes
import com.example.aplicacion_hotel.ViewModel.RegisterViewModel
import com.example.aplicacion_hotel.ViewModel.RegisterViewModelFactory
import com.example.aplicacion_hotel.utils.HotelSessionManager
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {


    val context = LocalContext.current
    val hotelSessionManager = remember { HotelSessionManager(context) }

    val viewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(hotelSessionManager)
    )

    LaunchedEffect(viewModel.registerSuccess) {
        if (viewModel.registerSuccess) {
            navController.navigate(Routes.Home.route) {
                popUpTo(Routes.Register.route) { inclusive = true }
            }
        }
    }

    var nombre by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }

    var sexo by remember { mutableStateOf("") }
    var txtSexo by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") }
        )

        OutlinedTextField(
            value = dni,
            onValueChange = { dni = it },
            label = { Text("DNI") }
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") }
        )

        OutlinedTextField(
            value = fechaNacimiento,
            onValueChange = {},
            readOnly = true,
            enabled = false, // ✅ evita que el TextField “se coma” el click
            label = { Text("Fecha de nacimiento") },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Elegir fecha")
                }
            },
            modifier = Modifier
        )


        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = txtSexo,
                onValueChange = {},
                readOnly = true,
                label = { Text("Sexo") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Hombre") },
                    onClick = {
                        sexo = "M"
                        txtSexo = "Hombre"
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Mujer") },
                    onClick = {
                        sexo = "F"
                        txtSexo = "Mujer"
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Otro") },
                    onClick = {
                        sexo = "X"
                        txtSexo = "Otro"
                        expanded = false
                    }
                )
            }
        }

        OutlinedTextField(
            value = ciudad,
            onValueChange = { ciudad = it },
            label = { Text("Ciudad") }
        )

        Row {

            Button(
                onClick = {
                    viewModel.register(
                        nombre.trim(),
                        dni.trim(),
                        email.trim(),
                        password,
                        fechaNacimiento,
                        sexo,
                        ciudad.trim()
                    )
                },
                enabled = !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Crear cuenta")
                }
            }

            Button(
                onClick = { navController.popBackStack() },
                enabled = !viewModel.isLoading
            ) {
                Text("Volver")
            }
        }

        viewModel.errorMessage?.let { msg ->
            Text(
                text = msg,
                color = androidx.compose.ui.graphics.Color.Red
            )
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
                        fechaNacimiento = formatter.format(Date(millis))
                    }
                    showDatePicker = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}