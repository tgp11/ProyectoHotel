package com.example.aplicacion_hotel.utils

import android.content.Context
import com.example.aplicacion_hotel.Model.Cliente
import com.google.gson.Gson

class HotelSessionManager(context: Context) {

<<<<<<< HEAD
    private val prefs = context.getSharedPreferences("hotel_session", Context.MODE_PRIVATE)
=======
    private val prefs =
        context.getSharedPreferences("hotel_session", Context.MODE_PRIVATE)

>>>>>>> 62a9fd2950d9fb3aaa5d6b3538f4896430af0836
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

<<<<<<< HEAD
    fun getUserId(): String? {
        return getCliente()?._id
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
=======
    fun logout() {
        prefs.edit().clear().apply()
    }
    fun getUserId(): String? {
        return prefs.getString("userId", null)
    }
}
>>>>>>> 62a9fd2950d9fb3aaa5d6b3538f4896430af0836
