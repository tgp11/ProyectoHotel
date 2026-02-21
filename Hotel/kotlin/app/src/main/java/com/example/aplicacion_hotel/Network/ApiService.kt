package com.example.aplicacion_hotel.Network

import com.example.aplicacion_hotel.Model.Cliente
import com.example.aplicacion_hotel.Model.Habitacion
import com.example.aplicacion_hotel.Model.LoginRequest
import com.example.aplicacion_hotel.Model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.example.aplicacion_hotel.Model.CrearReservaRequest
import com.example.aplicacion_hotel.Model.Reserva
import retrofit2.Response

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

    @GET("reservas/{clienteId}")
    suspend fun obtenerReservasUsuario(
        @Path("clienteId") clienteId: String
    ): Response<List<Reserva>>
}