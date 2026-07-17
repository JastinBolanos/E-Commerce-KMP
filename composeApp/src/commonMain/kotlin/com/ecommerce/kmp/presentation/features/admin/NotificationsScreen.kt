package com.ecommerce.kmp.presentation.features.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import androidx.compose.foundation.clickable
import com.ecommerce.kmp.domain.model.Order
import e_commercekmp.composeapp.generated.resources.Res
import e_commercekmp.composeapp.generated.resources.imperial_script

/**
 * ============================================================================
 * 🔔 ADMIN NOTIFICATIONS INBOX & ALERTS
 * ============================================================================
 * * @description
 * This screen acts as the real-time alert inbox for the Store Administrator.
 * It observes the global stream of incoming purchases and filters them to
 * display only actionable, "Pending" orders.
 * It features empty-state handling and a custom, lightweight timestamp parser
 * to convert raw epoch milliseconds into a localized timezone (UTC-5) format.
 * * 🔌 NOTE FOR BACKEND / INFRASTRUCTURE TEAM:
 * Currently, this inbox updates reactively via the in-memory `StateFlow`.
 * In a production cloud environment, this screen must be supported by native
 * Push Notifications (Firebase Cloud Messaging - FCM, or Apple APNs).
 * When a background push payload arrives from the server, the app should
 * trigger a silent background fetch (`GET /api/v1/orders?status=pending`)
 * to update the local repository, which will instantly and automatically
 * recompose this UI without user intervention.
 * * @layer Presentation / Features / Admin
 * ============================================================================
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    pendingOrders: List<Order>,
    onBackClick: () -> Unit,
    onNotificationClick: (Order) -> Unit
) {
    val imperialFont = FontFamily(Font(Res.font.imperial_script))

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text("Notifications", fontFamily = imperialFont, fontSize = 36.sp, color = Color.Black)
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = Color.Black)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
                HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            }
        }
    ) { paddingValues ->

        // --- NOTIFICATIONS LIST ---
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color.White)
        ) {
            items(pendingOrders) { order ->
                NotificationItemReal(
                    order = order,
                    onClick = { onNotificationClick(order) }
                )
                HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            }

            if (pendingOrders.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("You have no new notifications.", color = Color.Gray)
                    }
                }
            }
        }
    }
}

// --- COMPONENT: NOTIFICATION ITEM ---
@Composable
fun NotificationItemReal(order: Order, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color(0xFFF9F9F9))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Alert Dot
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(Color(0xFF5474FF))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New Payment: ${order.id.takeLast(4)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = formatOrderTime(order.timestamp),
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${order.customerName} has sent a receipt for $ ${order.totalAmount.format(2)} and awaits your confirmation.",
                fontSize = 13.sp,
                color = Color.DarkGray,
                lineHeight = 18.sp
            )
        }
    }
}

// --- UTILITY: TIME TRANSLATOR FOR PERU (UTC-5) ---
fun formatOrderTime(millis: Long): String {
    if (millis == 0L) return "Just now"

    val totalSeconds = millis / 1000
    val currentSeconds = totalSeconds % (24 * 3600)

    // Calculate the time in UTC-5 (Lima, Peru Time)
    val hours = ((currentSeconds / 3600) - 5 + 24) % 24
    val minutes = (currentSeconds % 3600) / 60

    val amPm = if (hours >= 12) "PM" else "AM"
    val displayHour = if (hours % 12 == 0L) 12L else hours % 12
    val minStr = if (minutes < 10) "0$minutes" else "$minutes"

    return "$displayHour:$minStr $amPm"
}

private fun Double.format(digits: Int): String {
    val rounded = (this * 100).toLong() / 100.0
    val parts = rounded.toString().split(".")
    val whole = parts[0]
    val fraction = if (parts.size > 1) parts[1] else "0"
    return "$whole.${fraction.padEnd(digits, '0')}"
}