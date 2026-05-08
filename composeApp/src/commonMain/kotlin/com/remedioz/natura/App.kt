package com.remedioz.natura

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.remedioz.natura.ui.screens.AdminScreen
import com.remedioz.natura.ui.screens.home.HomeScreen

@Composable
fun App() {
    MaterialTheme {
        // La variable de estado que controla qué pantalla vemos
        var currentScreen by remember { mutableStateOf("STORE") }

        if (currentScreen == "STORE") {
            HomeScreen(
                onAdminClick = {
                    currentScreen = "ADMIN"
                }
            )
        } else if (currentScreen == "ADMIN") {
            AdminScreen(
                onBackClick = { currentScreen = "STORE" }
            )
        }
    }
}