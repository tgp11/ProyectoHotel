package com.example.aplicacion_hotel.View.screens.login

import AuthViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.navigation.NavController
import com.example.aplicacion_hotel.View.navigation.Routes
import com.example.aplicacion_hotel.ViewModel.AuthViewModelFactory
import com.example.aplicacion_hotel.utils.HotelSessionManager
import androidx.compose.material3.SnackbarHostState

@Composable
fun LoginScreen(navController: NavController) {

    val context = LocalContext.current
    val hotelSessionManager = remember { HotelSessionManager(context) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(hotelSessionManager)
    )
    LaunchedEffect(viewModel.loginSuccess) {
        if (viewModel.loginSuccess) {
            navController.navigate(Routes.Home.route) {
                popUpTo(Routes.Login.route) { inclusive = true }
            }
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            // opcional: limpiar el error si tu VM tiene función para ello
            // viewModel.clearError()
        }
    }



    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
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
        Row {
            Button(
                onClick = {
                    viewModel.login(email.trim(), password.trim())
                },
                enabled = !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Iniciar sesión")
                }

            }
            Button(onClick = {
                navController.navigate(Routes.Register.route)
            }) {
                Text("Crear cuenta")
            }

        }
        if (viewModel.errorMessage != null) {
            Text(
                text = viewModel.errorMessage!!,
                color = androidx.compose.ui.graphics.Color.Red
            )
        }

        /*Button(onClick = {
            navController.navigate(Routes.Home.route)
        }) {
            Text("Iniciar sesión")
        }

        Button(onClick = {
            navController.navigate(Routes.Register.route)
        }) {
            Text("Crear cuenta")
        }*/
    }

}