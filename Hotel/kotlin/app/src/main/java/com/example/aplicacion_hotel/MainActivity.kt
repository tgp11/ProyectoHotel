package com.example.aplicacion_hotel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.aplicacion_hotel.View.navigation.AppNavigation
import com.example.aplicacion_hotel.utils.HotelSessionManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val hotelSessionManager = HotelSessionManager(this)
            val startDestination =
                if (hotelSessionManager.getToken() != null) "home"
                else "login"

            AppNavigation()
        }
    }
}
