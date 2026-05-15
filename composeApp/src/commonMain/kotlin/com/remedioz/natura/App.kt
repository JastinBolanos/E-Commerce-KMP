package com.remedioz.natura

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remedioz.natura.data.repository.ProductRepositoryImpl
import com.remedioz.natura.ui.screens.AdminScreen
import com.remedioz.natura.ui.screens.home.HomeScreen
import com.remedioz.natura.ui.viewmodel.AdminViewModel
import com.remedioz.natura.ui.viewmodel.HomeViewModel
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor2.KtorNetworkFetcherFactory

@Composable
fun App() {

    // --- NUEVO: LE ENSEÑAMOS A COIL A DESCARGAR DE INTERNET ---
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .build()
    }

    MaterialTheme {
        // Variable de estado que controla qué pantalla vemos
        var currentScreen by remember { mutableStateOf("STORE") }

        // 1. Creamos el repositorio UNA vez aquí arriba para que ambas pantallas lo puedan usar
        val productRepository = remember { ProductRepositoryImpl() }

        // Envolvemos todo en un Box con safeDrawingPadding() para respetar la barra de estado
        Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {

            if (currentScreen == "STORE") {
                val homeViewModel = viewModel { HomeViewModel(productRepository) }

                HomeScreen(
                    onAdminClick = {
                        currentScreen = "ADMIN"
                    },
                    viewModel = homeViewModel
                )

            } else if (currentScreen == "ADMIN") {
                val adminViewModel = viewModel { AdminViewModel(productRepository) }

                AdminScreen(
                    onBackClick = { currentScreen = "STORE" },
                    viewModel = adminViewModel
                )
            }
        }
    }
}