package com.example.aplicacion_hotel.View.screens.perfil
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.aplicacion_hotel.Network.RetrofitInstance
import com.example.aplicacion_hotel.ViewModel.PerfilViewModel
import com.example.aplicacion_hotel.ViewModel.PerfilViewModelFactory
import com.example.aplicacion_hotel.utils.HotelSessionManager

@Composable
fun PerfilScreen(navController: NavHostController) {

    val context = LocalContext.current
    val hotelSessionManager = remember { HotelSessionManager(context) }

    val viewModel: PerfilViewModel = viewModel(
        factory = PerfilViewModelFactory(hotelSessionManager)
    )

    LaunchedEffect(Unit) {
        viewModel.loadCliente()
    }

    val cliente = viewModel.cliente

    if (cliente == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay sesión iniciada")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // FOTO PERFIL
        if (!cliente.foto.isNullOrEmpty()) {

            AsyncImage(
                model = "${RetrofitInstance.BASE_URL.dropLast(1)}${cliente.foto}",
                contentDescription = "Foto perfil",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
            )

        } else {

            Surface(
                modifier = Modifier.size(140.dp),
                shape = CircleShape,
                tonalElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Usuario",
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = cliente.nombre,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        PerfilItem("Email", cliente.email)
        PerfilItem("DNI", cliente.dni)
        PerfilItem(
            "Fecha nacimiento",
            viewModel.formatearFecha(cliente.fechaNacimiento)
        )
        PerfilItem(
            "Sexo",
            viewModel.sexoTexto(cliente.sexo)
        )
        PerfilItem("Ciudad", cliente.ciudad)
        PerfilItem("VIP", if (cliente.vip) "Sí" else "No")

    }
}