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
                        Text("Notificaciones", fontFamily = imperialFont, fontSize = 36.sp, color = Color.Black)
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Volver", tint = Color.Black)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
                HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            }
        }
    ) { paddingValues ->

        // --- LISTA DE NOTIFICACIONES ---
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
                        Text("No tienes notificaciones nuevas.", color = Color.Gray)
                    }
                }
            }
        }
    }
}

// --- COMPONENTE: ITEM DE NOTIFICACIÓN ---
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
        // Punto de Alerta
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
                    text = "Nuevo Pago: ${order.id.takeLast(4)}",
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
                text = "${order.customerName} ha enviado un comprobante por S/ ${order.totalAmount} y espera tu confirmación.",
                fontSize = 13.sp,
                color = Color.DarkGray,
                lineHeight = 18.sp
            )
        }
    }
}

// --- HERRAMIENTA: TRADUCTOR DE HORA PARA PERÚ (UTC-5) ---
fun formatOrderTime(millis: Long): String {
    if (millis == 0L) return "Reciente"

    val totalSeconds = millis / 1000
    val currentSeconds = totalSeconds % (24 * 3600)

    // Calculamos la hora en UTC-5 (Hora de Lima, Perú)
    val hours = ((currentSeconds / 3600) - 5 + 24) % 24
    val minutes = (currentSeconds % 3600) / 60

    val amPm = if (hours >= 12) "PM" else "AM"
    val displayHour = if (hours % 12 == 0L) 12L else hours % 12
    val minStr = if (minutes < 10) "0$minutes" else "$minutes"

    return "$displayHour:$minStr $amPm"
}