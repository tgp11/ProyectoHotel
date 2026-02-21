package com.example.aplicacion_hotel.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion_hotel.Model.Habitacion
import com.example.aplicacion_hotel.Repository.HabitacionRepository
import com.example.aplicacion_hotel.utils.SessionManager
import kotlinx.coroutines.launch

class HabitacionesViewModel(
    private val sessionManager: SessionManager,
    private val repository: HabitacionRepository = HabitacionRepository()
) : ViewModel() {

    private val _habitaciones = mutableStateOf<List<Habitacion>>(emptyList())
    val habitaciones: State<List<Habitacion>> = _habitaciones

    private val _cargando = mutableStateOf(false)
    val cargando: State<Boolean> = _cargando

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _idsCarrito = mutableStateOf(sessionManager.getCarritoIds())
    val idsCarrito: State<Set<String>> = _idsCarrito

    fun cargarHabitaciones(soloDisponibles: Boolean = true) {
        viewModelScope.launch {
            try {
                _cargando.value = true
                _error.value = null

                val todas = repository.getHabitaciones()
                _habitaciones.value = if (soloDisponibles) {
                    todas.filter { it.disponible }
                } else {
                    todas
                }

                // refresca carrito desde prefs por si cambió
                _idsCarrito.value = sessionManager.getCarritoIds()

            } catch (e: Exception) {
                _error.value = "Error al cargar habitaciones: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun toggleCarrito(idHabitacion: String) {
        _idsCarrito.value = sessionManager.toggleCarrito(idHabitacion)
    }
}