package com.example.aplicacion_hotel.View.Scaffold

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.aplicacion_hotel.View.navigation.Routes
import com.example.aplicacion_hotel.utils.HotelSessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelTopBar(
    navController: NavController
) {
    val context = LocalContext.current
    val session = remember { HotelSessionManager(context) } //ahora existe

    var expanded by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("Hotel App") },
        actions = {
            IconButton(onClick = { navController.navigate(Routes.InfoHotel.route) }) {
                Icon(Icons.Default.Info, contentDescription = "Info")
            }

            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Perfil")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Ver perfil") },
                    onClick = {
                        expanded = false
                        navController.navigate(Routes.Perfil.route)
                    }
                )

                DropdownMenuItem(
                    text = { Text("Editar perfil") },
                    onClick = {
                        expanded = false
                        navController.navigate(Routes.EditarPerfil.route)
                    }
                )

                DropdownMenuItem(
                    text = { Text("Cerrar sesión") },
                    onClick = {
                        expanded = false
                        showLogoutDialog = true
                    }
                )
            }
        }
    )

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro de que quieres cerrar sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        session.logout() //ahora compila

                        navController.navigate(Routes.Login.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                ) { Text("Sí") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("No") }
            }
        )
    }
}