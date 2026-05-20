package com.remedioz.natura

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor2.KtorNetworkFetcherFactory
import com.remedioz.natura.data.repository.FirebaseRepository
import com.remedioz.natura.data.repository.ProductRepositoryImpl
import com.remedioz.natura.ui.screens.AdminScreen
import com.remedioz.natura.ui.screens.AuthScreen
import com.remedioz.natura.ui.screens.home.HomeScreen
import com.remedioz.natura.ui.viewmodel.AdminViewModel
import com.remedioz.natura.ui.viewmodel.AuthViewModel
import com.remedioz.natura.ui.viewmodel.HomeViewModel

@Composable
fun App() {

    // --- LE ENSEÑAMOS A COIL A DESCARGAR DE INTERNET ---
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .build()
    }

    MaterialTheme {
        // Variable de estado global que controla qué pantalla vemos
        var currentScreen by remember { mutableStateOf("STORE") }

        // --- REPOSITORIOS (Se instancian una sola vez para ahorrar memoria) ---
        val productRepository = remember { ProductRepositoryImpl() }
        val firebaseRepository = remember { FirebaseRepository() } // <-- NUEVO: Para la Autenticación

        // Envolvemos todo en un Box con safeDrawingPadding() para respetar la barra de estado
        Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {

            if (currentScreen == "STORE") {
                val homeViewModel = viewModel { HomeViewModel(productRepository) }

                HomeScreen(
                    onAdminClick = { currentScreen = "ADMIN" },
                    onAuthClick = { currentScreen = "AUTH" },
                    viewModel = homeViewModel
                )

            } else if (currentScreen == "ADMIN") {
                val adminViewModel = viewModel { AdminViewModel(productRepository) }

                AdminScreen(
                    onBackClick = { currentScreen = "STORE" },
                    viewModel = adminViewModel
                )

            } else if (currentScreen == "AUTH") {
                // --- PANTALLA DE LOGIN ---
                val authViewModel = viewModel { AuthViewModel(firebaseRepository) }

                AuthScreen(
                    viewModel = authViewModel,
                    onClose = { currentScreen = "STORE" },
                    onGoogleSignInClick = {
                        println("Disparando Popup de Google...")
                        // TODO: Aquí implementaremos el puente nativo de Google pronto
                    }
                )
            }
        }
    }
}