package com.example.aplicacion_hotel.Network

import com.example.aplicacion_hotel.Model.Cliente
import com.example.aplicacion_hotel.Model.Habitacion
import com.example.aplicacion_hotel.Model.LoginRequest
import com.example.aplicacion_hotel.Model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import com.example.aplicacion_hotel.Model.CrearReservaRequest
import com.example.aplicacion_hotel.Model.Reserva
import retrofit2.Response
import retrofit2.http.Query

interface ApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("habitaciones")
    suspend fun getHabitaciones(): List<Habitacion>

    @GET("cliente/{id}")
    suspend fun getClienteById(
        @Path("id") id: String
    ): Cliente

    @POST("cliente")
    suspend fun crearCliente(
        @Body cliente: Cliente
    ): Cliente

    @POST("reservas")
    suspend fun crearReserva(
        @Body reserva: CrearReservaRequest
    ): Response<Unit>

    // Obtener todas las reservas (para filtrar disponibilidad localmente)
    @GET("reservas")
    suspend fun obtenerTodasLasReservas(): Response<List<Reserva>>

    @GET("reservas")
    suspend fun obtenerReservasUsuario(
        @Query("clienteId") clienteId: String
    ): Response<List<Reserva>>

    // --- CORREGIDO PARA TU BACKEND ---
    @PUT("reservas/{id}/cancelar")
    suspend fun cancelarReserva(
        @Path("id") id: String
    ): Response<Reserva>
}
