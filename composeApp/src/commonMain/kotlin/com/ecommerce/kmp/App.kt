package com.ecommerce.kmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ecommerce.kmp.data.repository.MockOrderRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.ecommerce.kmp.data.repository.MockProductRepositoryImpl
import com.ecommerce.kmp.presentation.components.BackHandler
import com.ecommerce.kmp.presentation.features.admin.AdminScreen
import com.ecommerce.kmp.presentation.features.admin.AdminViewModel
import com.ecommerce.kmp.presentation.features.admin.EditProductsScreen
import com.ecommerce.kmp.presentation.features.admin.NotificationsScreen
import com.ecommerce.kmp.presentation.features.admin.OrderDetailsScreen
import com.ecommerce.kmp.presentation.features.admin.OrdersScreen
import com.ecommerce.kmp.presentation.features.admin.OrdersViewModel
import com.ecommerce.kmp.presentation.features.admin.ShippingTimelineScreen
import com.ecommerce.kmp.presentation.features.admin.UpdatePaymentScreen
import com.ecommerce.kmp.presentation.features.auth.AuthScreen
import com.ecommerce.kmp.presentation.features.auth.AuthViewModel
import com.ecommerce.kmp.presentation.features.checkout.CheckoutViewModel
import com.ecommerce.kmp.presentation.features.home.HomeScreen
import com.ecommerce.kmp.presentation.features.home.HomeViewModel
import com.ecommerce.kmp.presentation.features.profile.CustomerProfileScreen
import com.ecommerce.kmp.presentation.features.profile.CustomerTimelineScreen

@Composable
fun App() {

    MaterialTheme {
        var currentScreen by remember { mutableStateOf("STORE") }
        var selectedOrder by remember { mutableStateOf<com.ecommerce.kmp.domain.model.Order?>(null) }
        var adminOrdersTab by remember { mutableIntStateOf(0) }

        // Simulación de sesión local para el portafolio
        var isUserLoggedIn by remember { mutableStateOf(false) }

        // --- INYECCIÓN DE DEPENDENCIAS (NUESTRAS BASES DE DATOS FALSAS) ---
        val productRepository = remember { MockProductRepositoryImpl() }
        val orderRepository = remember { MockOrderRepositoryImpl() }

        Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {

            when (currentScreen) {
                "STORE" -> {
                    val homeViewModel = viewModel { HomeViewModel(productRepository) }
                    val checkoutViewModel = viewModel { CheckoutViewModel() } // Ya no necesita Firebase

                    HomeScreen(
                        onAdminClick = { currentScreen = "AUTH" },
                        onAuthClick = {
                            if (isUserLoggedIn) {
                                currentScreen = "PROFILE"
                            } else {
                                currentScreen = "AUTH"
                            }
                        },
                        viewModel = homeViewModel,
                        checkoutViewModel = checkoutViewModel
                    )
                }

                "ADMIN" -> {
                    BackHandler { currentScreen = "STORE" }

                    val ordersViewModel = viewModel { OrdersViewModel(orderRepository) }
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
                    BackHandler { currentScreen = "ADMIN" }

                    val adminViewModel = viewModel { AdminViewModel(productRepository) }

                    EditProductsScreen(
                        onBackClick = { currentScreen = "ADMIN" },
                        viewModel = adminViewModel
                    )
                }

                "ORDERS" -> {
                    BackHandler { currentScreen = "ADMIN" }

                    val ordersViewModel = viewModel { OrdersViewModel(orderRepository) }
                    val orders by ordersViewModel.orders.collectAsState()

                    OrdersScreen(
                        orders = orders,
                        selectedTab = adminOrdersTab,
                        onTabChange = { adminOrdersTab = it },
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
                    BackHandler { currentScreen = "ORDERS" }

                    val ordersViewModel = viewModel { OrdersViewModel(orderRepository) }
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
                    BackHandler { currentScreen = "ORDERS" }

                    val ordersViewModel = viewModel { OrdersViewModel(orderRepository) }
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
                    BackHandler { currentScreen = "ADMIN" }

                    val coroutineScope = rememberCoroutineScope()
                    var isUploadingQr by remember { mutableStateOf(false) }

                    // Estados locales simulados para la UI
                    var currentPhone by remember { mutableStateOf("999 888 777") }

                    UpdatePaymentScreen(
                        currentQrUrl = "", // Omitido por ser local
                        currentPhoneNumber = currentPhone,
                        isLoading = isUploadingQr,
                        onBackClick = { currentScreen = "ADMIN" },
                        onSaveClick = { newQrBytes, newPhoneNumber ->
                            coroutineScope.launch {
                                isUploadingQr = true
                                delay(1500) // Simular subida
                                currentPhone = newPhoneNumber
                                isUploadingQr = false
                                currentScreen = "ADMIN"
                            }
                        }
                    )
                }

                "NOTIFICATIONS" -> {
                    BackHandler { currentScreen = "ADMIN" }

                    val ordersViewModel = viewModel { OrdersViewModel(orderRepository) }
                    val orders by ordersViewModel.orders.collectAsState()
                    val pendingOrders = orders.filter { it.status.equals("Pendiente", ignoreCase = true) }

                    NotificationsScreen(
                        pendingOrders = pendingOrders,
                        onBackClick = { currentScreen = "ADMIN" },
                        onNotificationClick = { order ->
                            selectedOrder = order
                            currentScreen = "ORDER_DETAILS"
                        }
                    )
                }

                "AUTH" -> {
                    BackHandler { currentScreen = "STORE" }

                    val authViewModel = viewModel { AuthViewModel() }

                    AuthScreen(
                        viewModel = authViewModel,
                        onClose = { currentScreen = "STORE" },

                        onClientSuccess = {
                            isUserLoggedIn = true
                            currentScreen = "PROFILE"
                        }
                    )
                }

                "PROFILE" -> {
                    BackHandler { currentScreen = "STORE" }

                    val ordersViewModel = viewModel { OrdersViewModel(orderRepository) }
                    val allOrders by ordersViewModel.orders.collectAsState()
                    val authViewModel = viewModel { AuthViewModel() }

                    val userName by authViewModel.userName.collectAsState()
                    val userEmail by authViewModel.userEmail.collectAsState()
                    val userPhotoUrl by authViewModel.userPhotoUrl.collectAsState()

                    CustomerProfileScreen(
                        userName = userName,
                        userEmail = userEmail,
                        userPhotoUrl = userPhotoUrl,
                        orders = allOrders,
                        onBackClick = { currentScreen = "STORE" },
                        onTrackOrderClick = { order ->
                            selectedOrder = order
                            currentScreen = "CUSTOMER_TIMELINE"
                        }
                    )
                }

                "CUSTOMER_TIMELINE" -> {
                    BackHandler { currentScreen = "PROFILE" }

                    selectedOrder?.let { order ->
                        CustomerTimelineScreen(
                            orderId = order.id,
                            currentStatus = order.status,
                            onBackClick = { currentScreen = "PROFILE" }
                        )
                    }
                }
            }
        }
    }
}