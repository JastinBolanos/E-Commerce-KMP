package com.ecommerce.kmp.presentation.features.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import e_commercekmp.composeapp.generated.resources.Res
import e_commercekmp.composeapp.generated.resources.imperial_script
import org.jetbrains.compose.resources.Font

/**
 * ============================================================================
 * 🛠️ ADMIN DASHBOARD & ROUTING HUB
 * ============================================================================
 * * @description
 * This screen serves as the primary navigation hub for users with Administrative
 * privileges. It provides a clean, list-based routing menu to access order
 * management, product catalog editing, and payment configuration settings.
 * * Key UX Features implemented:
 * - Reactive Notification Badge: Utilizes Material 3 `BadgedBox` hooked to the
 * `pendingOrdersCount` state. It dynamically updates the unread order counter
 * in real-time as new purchases flow into the global state without requiring
 * manual screen refreshes or pull-to-refresh gestures.
 * * 🔌 NOTE FOR BACKEND / ROUTING TEAM:
 * This screen currently acts as a stateless view (purely relying on lambda
 * callbacks for navigation). If integrating a robust navigation library
 * (e.g., Voyager, Jetpack Navigation), replace the `onNavigateTo...` lambdas
 * with direct Navigator push/replace calls.
 * Ensure the `pendingOrdersCount` parameter is driven by a real-time stream
 * (e.g., Firestore snapshot listener or WebSocket subscription) fetching the
 * count of orders where `status == "Pendiente"`.
 * * @layer Presentation / Features / Admin
 * ============================================================================
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    pendingOrdersCount: Int,
    onBackClick: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToEditProducts: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToUpdatePayment: () -> Unit
) {
    val imperialFont = FontFamily(Font(Res.font.imperial_script))

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Administrator Mode",
                            fontFamily = imperialFont,
                            fontSize = 36.sp,
                            color = Color.Black
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Return", tint = Color.Black)
                        }
                    },
                    actions = {
                        IconButton(onClick = onNavigateToNotifications) {
                            if (pendingOrdersCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge(containerColor = Color(0xFF5474FF), contentColor = Color.White) {
                                            Text(pendingOrdersCount.toString())
                                        }
                                    }
                                ) {
                                    Icon(Icons.Outlined.Notifications, contentDescription = "Notifications", tint = Color.Black)
                                }
                            } else {
                                Icon(Icons.Outlined.Notifications, contentDescription = "Notifications", tint = Color.Black)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
                HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // --- ITEM 1: ORDERS ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToOrders() }
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "Orders",
                    fontFamily = imperialFont,
                    fontSize = 32.sp,
                    color = Color.Black
                )
            }
            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)

            // --- ITEM 2: EDIT PRODUCTS ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToEditProducts() }
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "Edit Products",
                    fontFamily = imperialFont,
                    fontSize = 32.sp,
                    color = Color.Black
                )
            }
            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)

            // --- ITEM 3: UPDATE PAYMENT METHOD ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToUpdatePayment() }
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "Update Payment Methods",
                    fontFamily = imperialFont,
                    fontSize = 32.sp,
                    color = Color.Black
                )
            }
            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
        }
    }
}