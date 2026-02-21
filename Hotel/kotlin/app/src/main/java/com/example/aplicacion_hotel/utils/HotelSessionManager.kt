package com.example.aplicacion_hotel.utils

import android.content.Context
import com.example.aplicacion_hotel.Model.Cliente
import com.google.gson.Gson

class HotelSessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("hotel_session", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("token", null)
    }

    fun saveCliente(cliente: Cliente) {
        val json = gson.toJson(cliente)
        prefs.edit().putString("cliente", json).apply()
    }

    fun getCliente(): Cliente? {
        val json = prefs.getString("cliente", null)
        return if (json != null) {
            gson.fromJson(json, Cliente::class.java)
        } else null
    }

    fun getUserId(): String? {
        return getCliente()?._id
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
