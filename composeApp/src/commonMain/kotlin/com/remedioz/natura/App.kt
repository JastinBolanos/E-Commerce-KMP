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
import com.remedioz.natura.presentation.features.admin.OrderDetailsScreen
import com.remedioz.natura.presentation.features.admin.OrdersScreen
import com.remedioz.natura.presentation.features.admin.OrdersViewModel
import com.remedioz.natura.presentation.features.admin.UpdatePaymentScreen
import com.remedioz.natura.presentation.features.auth.AuthViewModel
import com.remedioz.natura.presentation.features.checkout.CheckoutViewModel
import com.remedioz.natura.presentation.features.home.HomeViewModel
import com.remedioz.natura.presentation.features.admin.ShippingTimelineScreen
import kotlinx.coroutines.launch

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
        var selectedOrder by remember { mutableStateOf<com.remedioz.natura.domain.model.Order?>(null) }
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
                    val ordersViewModel = viewModel { OrdersViewModel(firebaseRepository) }
                    val orders by ordersViewModel.orders.collectAsState()
                    val pendingCount = orders.count { it.status.equals("Pendiente", ignoreCase = true) }

                    AdminScreen(
                        pendingOrdersCount = pendingCount,
                        onBackClick = { currentScreen = "STORE" },
                        onNavigateToOrders = { currentScreen = "ORDERS" },
                        onNavigateToEditProducts = { currentScreen = "EDIT_PRODUCTS" },
                        onNavigateToNotifications = { currentScreen = "NOTIFICATIONS" },
                        onNavigateToUpdatePayment = { currentScreen = "UPDATE_PAYMENT" }
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
                    val ordersViewModel = viewModel { OrdersViewModel(firebaseRepository) }
                    val orders by ordersViewModel.orders.collectAsState()

                    OrdersScreen(
                        orders = orders,
                        onBackClick = { currentScreen = "ADMIN" },
                        onConfirmClick = { order ->
                            selectedOrder = order

                            if (order.status.equals("Pendiente", ignoreCase = true)) {
                                currentScreen = "ORDER_DETAILS"
                            } else {
                                currentScreen = "SHIPPING_TIMELINE"
                            }
                        }
                    )
                }

                "SHIPPING_TIMELINE" -> {
                    val ordersViewModel = viewModel { OrdersViewModel(firebaseRepository) }
                    val isLoading by ordersViewModel.isLoading.collectAsState()

                    selectedOrder?.let { order ->
                        ShippingTimelineScreen(
                            orderId = order.id,
                            customerName = order.customerName,
                            currentStatus = order.status,
                            isLoading = isLoading,
                            onBackClick = { currentScreen = "ORDERS" },
                            onUpdateStatusClick = { nextStatus ->
                                ordersViewModel.updateOrderStatus(order.id, nextStatus) {
                                    currentScreen = "ORDERS"
                                }
                            }
                        )
                    }
                }

                "ORDER_DETAILS" -> {
                    val ordersViewModel = viewModel { OrdersViewModel(firebaseRepository) }
                    val isLoading by ordersViewModel.isLoading.collectAsState()

                    selectedOrder?.let { order ->
                        OrderDetailsScreen(
                            orderId = order.id,
                            voucherUrl = order.voucherUrl,
                            customerName = order.customerName,
                            totalAmount = "S/ ${order.totalAmount}",
                            status = order.status,
                            isLoading = isLoading,
                            onBackClick = { currentScreen = "ORDERS" },
                            onApproveClick = { orderId ->
                                ordersViewModel.updateOrderStatus(orderId, "Aprobado") {
                                    currentScreen = "ORDERS"
                                }
                            },
                            onRejectClick = { orderId ->
                                ordersViewModel.updateOrderStatus(orderId, "Rechazado") {
                                    currentScreen = "ORDERS"
                                }
                            }
                        )
                    }
                }

                "UPDATE_PAYMENT" -> {
                    val coroutineScope = rememberCoroutineScope()
                    var isUploadingQr by remember { mutableStateOf(false) }
                    val paymentSettings by firebaseRepository.observePaymentSettings().collectAsState(initial = com.remedioz.natura.data.repository.PaymentSettings())

                    UpdatePaymentScreen(
                        currentQrUrl = paymentSettings.qrUrl,
                        currentPhoneNumber = paymentSettings.phoneNumber,
                        isLoading = isUploadingQr,
                        onBackClick = { currentScreen = "ADMIN" },
                        onSaveClick = { newQrBytes, newPhoneNumber ->
                            coroutineScope.launch {
                                isUploadingQr = true
                                val success = firebaseRepository.updatePaymentSettings(newQrBytes, paymentSettings.qrUrl, newPhoneNumber)
                                isUploadingQr = false

                                if (success) {
                                    currentScreen = "ADMIN"
                                }
                            }
                        }
                    )
                }

                "NOTIFICATIONS" -> {
                    // PANTALLA DE NOTIFICACIONES (Fase Demo)
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