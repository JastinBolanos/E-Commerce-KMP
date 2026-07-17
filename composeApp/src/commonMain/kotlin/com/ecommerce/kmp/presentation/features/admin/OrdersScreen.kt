package com.ecommerce.kmp.presentation.features.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecommerce.kmp.domain.model.Order
import com.ecommerce.kmp.presentation.components.ProductDetailDialog
import com.ecommerce.kmp.presentation.components.getKitImagePainter
import com.ecommerce.kmp.presentation.components.getProductImagePainter
import org.jetbrains.compose.resources.Font
import e_commercekmp.composeapp.generated.resources.Res
import e_commercekmp.composeapp.generated.resources.imperial_script

/**
 * ============================================================================
 * 📋 ADMIN ORDERS MANAGEMENT & TABBED ROUTING SCREEN
 * ============================================================================
 * * @description
 * This screen serves as the primary operational dashboard for the Store Admin
 * to track and process incoming orders. It implements a reactive `TabRow`
 * architecture to seamlessly filter the global order state into actionable
 * lists (Pending vs. In-Transit/Delivered) without triggering heavy recompositions
 * or new navigation events.
 * * Key UX Features implemented:
 * - Dynamic UI State Machine: Action buttons inside the `OrderCardReal` adapt
 * their color, text, and availability purely based on the domain-level `Order.status`.
 * - Safe Text Truncation: Prevents UI clipping on large carts using `TextOverflow.Ellipsis`.
 * - Shared Product Dialogs: Reuses the `ProductDetailDialog` to inspect items directly
 * from the order timeline.
 * * 🔌 NOTE FOR BACKEND / DATA TEAM:
 * The current filtering (`orders.filter { ... }`) happens entirely client-side
 * in the device's RAM. For a production environment with thousands of orders,
 * this Tab interaction should be refactored to trigger server-side pagination
 * queries (e.g., `GET /api/v1/orders?status=pending&page=1&limit=20`) to
 * conserve device memory and data bandwidth.
 * * @layer Presentation / Features / Admin
 * ============================================================================
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    orders: List<Order>,
    selectedTab: Int,
    onTabChange: (Int) -> Unit,
    onBackClick: () -> Unit,
    onConfirmClick: (Order) -> Unit
) {
    val imperialFont = FontFamily(Font(Res.font.imperial_script))
    val tabs = listOf("Pending Confirmation", "Shipping Status")

    val displayedOrders = if (selectedTab == 0) {
        orders.filter { it.status.equals("Pending", ignoreCase = true) }
    } else {
        orders.filter { !it.status.equals("Pending", ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Orders",
                            fontFamily = imperialFont,
                            fontSize = 36.sp,
                            color = Color.Black
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = Color.Black)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )

                // --- TABS ---
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Color.Black,
                            height = 2.dp
                        )
                    },
                    divider = { HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp) }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { onTabChange(index) },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 13.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index) Color.Black else Color.DarkGray
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        // --- REAL ORDERS LIST ---
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            items(displayedOrders) { order ->
                OrderCardReal(order = order, onConfirmClick = { onConfirmClick(order) })
                HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            }
            if (displayedOrders.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("There are no orders in this section.", color = Color.Gray)
                    }
                }
            }
        }
    }
}

// --- COMPONENT: REAL ORDER CARD ---
@Composable
fun OrderCardReal(order: Order, onConfirmClick: () -> Unit) {
    // 1. Extract the first product for the image and dialog
    val firstProduct = order.items.firstOrNull()?.product
    val firstProductImage = firstProduct?.imageUrl ?: ""
    val isKit = firstProduct?.category.equals("Kits", ignoreCase = true)

    // 2. Native image translator
    val imagePainter = if (isKit) {
        getKitImagePainter(firstProductImage)
    } else {
        getProductImagePainter(firstProductImage)
    }

    // 3. State to control the dialog
    var showDetailsDialog by remember { mutableStateOf(false) }

    val totalItems = order.items.sumOf { it.quantity }
    val productSummary = order.items.joinToString(", ") { "${it.quantity}x ${it.product.name}" }

    val isPending = order.status.equals("Pending", ignoreCase = true)
    val isDelivered = order.status.equals("Delivered", ignoreCase = true)

    val buttonColor = when {
        isPending -> Color(0xFFD9D9D9)
        isDelivered -> Color(0xFF4CAF50)
        else -> Color.Black
    }

    val buttonText = when {
        isPending -> "Go to Confirm"
        isDelivered -> "Completed"
        else -> "Manage Shipping"
    }

    Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // Left Column: PRODUCT Image
        Column(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF0F0F0))) {
                Image(
                    painter = imagePainter,
                    contentDescription = "Product",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Order: ${order.id.takeLast(4)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Right Column: Stats and Smart Button
        Column(modifier = Modifier.weight(1.5f)) {
            // Summary of purchased products
            Text(
                text = productSummary,
                fontSize = 12.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Statistics
            OutlinedStatRow(label = "Qty:", value = "$totalItems items")
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedStatRow(label = "Total:", value = "$ ${order.totalAmount.format(2)}")
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedStatRow(label = "Status:", value = order.status)
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "View Details",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.clickable {
                        showDetailsDialog = true
                    })

                Button(
                    onClick = onConfirmClick,
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(
                        text = buttonText,
                        color = if (isPending) Color.Black else Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }

    if (showDetailsDialog && firstProduct != null) {
        ProductDetailDialog(
            product = firstProduct,
            imagePainter = imagePainter,
            onDismiss = { showDetailsDialog = false }
        )
    }
}

// --- COMPONENT: STATISTIC BOX ---
@Composable
fun OutlinedStatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 11.sp, color = Color.Black, modifier = Modifier.weight(1f))
        Box(modifier = Modifier.width(1.dp).height(12.dp).background(Color(0xFFCCCCCC)))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = value, fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.Medium)
    }
}

private fun Double.format(digits: Int): String {
    val rounded = (this * 100).toLong() / 100.0
    val parts = rounded.toString().split(".")
    val whole = parts[0]
    val fraction = if (parts.size > 1) parts[1] else "0"
    return "$whole.${fraction.padEnd(digits, '0')}"
}