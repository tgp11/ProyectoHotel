package com.example.aplicacion_hotel.Network

import com.example.aplicacion_hotel.Model.Cliente
import com.example.aplicacion_hotel.Model.Habitacion
import com.example.aplicacion_hotel.Model.LoginRequest
import com.example.aplicacion_hotel.Model.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @GET("reservas")
    suspend fun obtenerReservasUsuario(
        @Query("clienteId") clienteId: String
    ): Response<List<Reserva>>

    // --- CORREGIDO PARA TU BACKEND ---
    // Tu backend espera /reservas/{id}/cancelar
    @PUT("reservas/{id}/cancelar")
    suspend fun cancelarReserva(
        @Path("id") id: String
    ): Response<Reserva>
}


    @Multipart
    @PUT("cliente/{id}")
    suspend fun actualizarCliente(
        @Path("id") id: String,

        @Part("nombre") nombre: RequestBody,
        @Part("dni") dni: RequestBody,
        @Part("email") email: RequestBody,
        @Part("fechaNacimiento") fechaNacimiento: RequestBody,
        @Part("sexo") sexo: RequestBody,
        @Part("ciudad") ciudad: RequestBody,
        @Part("vip") vip: RequestBody,

        // upload.single('foto') => fieldName "foto"
        @Part foto: MultipartBody.Part? = null
    ): Cliente
}