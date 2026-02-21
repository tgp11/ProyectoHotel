package com.example.aplicacion_hotel.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion_hotel.Model.Cliente
import com.example.aplicacion_hotel.Repository.AuthRepository
import com.example.aplicacion_hotel.Repository.ClienteRepository
import com.example.aplicacion_hotel.utils.httpErrorMessage
import com.example.aplicacion_hotel.utils.HotelSessionManager
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RegisterViewModel(
    private val hotelSessionManager: HotelSessionManager
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var registerSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private val clienteRepository = ClienteRepository()
    private val authRepository = AuthRepository()

    fun register(
        nombre: String,
        dni: String,
        email: String,
        password: String,
        fechaNacimiento: String,
        sexo: String,
        ciudad: String
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val nuevoCliente = Cliente(
                    nombre = nombre,
                    dni = dni,
                    email = email,
                    password = password,
                    fechaNacimiento = fechaNacimiento,
                    sexo = sexo,
                    ciudad = ciudad,
                    vip = false
                )

                // 1) Crear
                clienteRepository.crearCliente(nuevoCliente)

                // 2) Login auto
                val loginResponse = authRepository.login(email, password)

                if (loginResponse.usuario.tipoUsuario != "Cliente") {
                    errorMessage = "Error al iniciar sesión automática"
                    return@launch
                }

                // 3) Guardar token + cliente
                hotelSessionManager.saveToken(loginResponse.token)
                val clienteCompleto = clienteRepository.getClienteById(loginResponse.usuario.id)
                hotelSessionManager.saveCliente(clienteCompleto)

                registerSuccess = true

            } catch (e: HttpException) {
                errorMessage = httpErrorMessage(e) // aquí verás “DNI inválido”, “Email ya registrado”, etc.
            } catch (e: IOException) {
                errorMessage = "Error de red. Revisa tu conexión."
            } catch (e: Exception) {
                errorMessage = "Error inesperado: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}