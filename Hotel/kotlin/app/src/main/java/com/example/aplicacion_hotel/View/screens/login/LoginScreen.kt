package com.example.aplicacion_hotel.View.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.aplicacion_hotel.View.navigation.Routes

@Composable
fun LoginScreen(navController: NavController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Button(onClick = {
            navController.navigate(Routes.Home.route)
        }) {
            Text("Iniciar sesión")
        }

        Button(onClick = {
            navController.navigate(Routes.Register.route)
        }) {
            Text("Crear cuenta")
        }
    }
}