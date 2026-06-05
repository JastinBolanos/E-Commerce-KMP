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
import com.remedioz.natura.presentation.features.admin.AdminScreen
import com.remedioz.natura.presentation.features.admin.EditProductsScreen
import com.remedioz.natura.presentation.features.auth.AuthScreen
import com.remedioz.natura.presentation.features.home.HomeScreen
import com.remedioz.natura.presentation.features.admin.AdminViewModel
import com.remedioz.natura.presentation.features.admin.NotificationsScreen
import com.remedioz.natura.presentation.features.admin.OrdersScreen
import com.remedioz.natura.presentation.features.auth.AuthViewModel
import com.remedioz.natura.presentation.features.checkout.CheckoutViewModel
import com.remedioz.natura.presentation.features.home.HomeViewModel

@Composable
fun App() {

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .build()
    }

    MaterialTheme {
        var currentScreen by remember { mutableStateOf("STORE") }
        val productRepository = remember { ProductRepositoryImpl() }
        val firebaseRepository = remember { FirebaseRepository() }

        Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {

            when (currentScreen) {
                "STORE" -> {
                    val homeViewModel = viewModel { HomeViewModel(productRepository) }
                    val checkoutViewModel = viewModel { CheckoutViewModel(firebaseRepository) }

                    HomeScreen(
                        onAdminClick = { currentScreen = "ADMIN" },
                        onAuthClick = { currentScreen = "AUTH" },
                        viewModel = homeViewModel,
                        checkoutViewModel = checkoutViewModel
                    )
                }

                "ADMIN" -> {
                    // 🟢 EL NUEVO MENÚ PRINCIPAL
                    AdminScreen(
                        onBackClick = { currentScreen = "STORE" },
                        onNavigateToOrders = { currentScreen = "ORDERS" },
                        onNavigateToEditProducts = { currentScreen = "EDIT_PRODUCTS" },
                        onNavigateToNotifications = { currentScreen = "NOTIFICATIONS" } 
                    )
                }

                "EDIT_PRODUCTS" -> {
                    val adminViewModel = viewModel { AdminViewModel(productRepository) }

                    EditProductsScreen(
                        onBackClick = { currentScreen = "ADMIN" },
                        viewModel = adminViewModel
                    )
                }

                "ORDERS" -> {
                    // 🟢 PANTALLA DE PEDIDOS (Fase Demo)
                    OrdersScreen(
                        onBackClick = { currentScreen = "ADMIN" },
                        onConfirmClick = { orderId ->
                            // TODO: En el siguiente paso navegaremos a la pantalla de detalle del Voucher
                            println("Ir a confirmar pedido: $orderId")
                        }
                    )
                }

                "NOTIFICATIONS" -> {
                    // 🟢 PANTALLA DE NOTIFICACIONES (Fase Demo)
                    NotificationsScreen(
                        onBackClick = { currentScreen = "ADMIN" }
                    )
                }

                "AUTH" -> {
                    // --- PANTALLA DE LOGIN ---
                    val authViewModel = viewModel { AuthViewModel(firebaseRepository) }

                    AuthScreen(
                        viewModel = authViewModel,
                        onClose = { currentScreen = "STORE" },
                        onGoogleSignInClick = {
                            println("Disparando Popup de Google...")
                        }
                    )
                }
            }
        }
    }
}