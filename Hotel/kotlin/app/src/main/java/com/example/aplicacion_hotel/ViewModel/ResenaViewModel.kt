package com.example.aplicacion_hotel.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion_hotel.Model.CrearResenaRequest
import com.example.aplicacion_hotel.Repository.ResenaRepository
import kotlinx.coroutines.launch

class ResenaViewModel : ViewModel() {
    private val repository = ResenaRepository()

    private val _resenaEnviada = mutableStateOf<Boolean?>(null)
    val resenaEnviada: State<Boolean?> = _resenaEnviada

    fun enviarResena(clienteId: String, comentario: String, puntuacion: Int) {
        viewModelScope.launch {
            val request = CrearResenaRequest(clienteId, comentario, puntuacion)
            val exito = repository.crearResena(request)
            _resenaEnviada.value = exito
        }
    }

    fun resetEstado() {
        _resenaEnviada.value = null
    }
}
