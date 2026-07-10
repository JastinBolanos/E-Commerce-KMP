package com.ecommerce.kmp.presentation.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import e_commercekmp.composeapp.generated.resources.Res
import e_commercekmp.composeapp.generated.resources.imperial_script
import org.jetbrains.compose.resources.Font

/**
 * ============================================================================
 * 📦 CUSTOMER ORDER TRACKING & TIMELINE SCREEN
 * ============================================================================
 * * @description
 * This screen provides a visual state-machine representation of an Order's
 * fulfillment journey. It dynamically calculates the active step based on the
 * `currentStatus` string and renders a vertical step-by-step progress tracker.
 * * Key UX Features implemented:
 * - Dynamic color coding (Green for success, Blue for active, Red for exceptions).
 * - Intrinsic UI measurement for seamless vertical connector lines.
 * - Exception handling routing (Dedicated UI block for "Rejected" states
 * with a call-to-action for customer support).
 * * 🔌 NOTE FOR BACKEND / LOGISTICS TEAM:
 * The timeline progression relies on exact string matching from the `statusFlow`
 * list. When integrating with a real logistics or shipping API (like Shippo,
 * DHL, or a custom backend), ensure the server's status enums strictly map to
 * these strings, or update the `statusFlow` list to match the server's payload.
 * For optimal UX, this screen should be connected to a Real-Time stream
 * (WebSockets/FCM) to watch the status move live without manual refreshing.
 * * @layer Presentation / Features / Profile
 * ============================================================================
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerTimelineScreen(
    orderId: String,
    currentStatus: String,
    onBackClick: () -> Unit
) {
    val imperialFont = FontFamily(Font(Res.font.imperial_script))
    val statusFlow = listOf("Pendiente", "Aprobado", "Preparando Paquete", "En Camino", "Entregado")
    val isRejected = currentStatus.equals("Rechazado", ignoreCase = true)
    val currentIndex = if (isRejected) 0 else statusFlow.indexOfFirst { it.equals(currentStatus, ignoreCase = true) }.takeIf { it >= 0 } ?: 0

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { Text("Seguimiento", fontFamily = imperialFont, fontSize = 36.sp, color = Color.Black) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Volver", tint = Color.Black) }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
                HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color.White).verticalScroll(rememberScrollState()).padding(24.dp)
        ) {
            // --- 1. CABECERA DEL PEDIDO ---
            Text("Detalles de la Orden", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    CustomerTimelineDetailRow("ID Pedido:", orderId.uppercase())
                    Spacer(modifier = Modifier.height(8.dp))

                    val statusColor = when {
                        currentStatus.equals("Entregado", ignoreCase = true) -> Color(0xFF4CAF50)
                        isRejected -> Color(0xFFD32F2F)
                        else -> Color(0xFF1976D2)
                    }
                    CustomerTimelineDetailRow("Estado Actual:", currentStatus, valueColor = statusColor)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // --- 2. LA LÍNEA DE TIEMPO VISUAL ---
            Text("Progreso del Envío", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(24.dp))

            statusFlow.forEachIndexed { index, stepName ->
                val isCompleted = index <= currentIndex && !isRejected
                val isLast = index == statusFlow.lastIndex
                val isActive = index == currentIndex
                val isStepRejected = isRejected && index == 0

                CustomerTimelineStep(
                    title = stepName,
                    isCompleted = isCompleted,
                    isLast = isLast,
                    isActive = isActive,
                    isRejected = isStepRejected
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // --- 3. MENSAJE FINAL AMIGABLE O DE ALERTA ---
            if (currentStatus.equals("Entregado", ignoreCase = true)) {
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp)).padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("¡Gracias por tu compra! Disfruta tus productos Natura.", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
            } else if (isRejected) {
                // CAJA DE RECHAZO CON AYUDA
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFFFEBEE), RoundedCornerShape(12.dp)).padding(16.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Tu pedido fue rechazado.", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Reportar problema o ayuda",
                            color = Color(0xFFB71C1C),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                // TODO: Aquí más adelante puedes abrir WhatsApp o un modal
                            }
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFF0F0F0), RoundedCornerShape(12.dp)).padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("Te notificaremos cuando el estado de tu pedido se actualice.", color = Color.DarkGray, fontSize = 13.sp, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

// COMPONENTE: DIBUJA LA BOLITA Y LÍNEA PARA EL CLIENTE
@Composable
private fun CustomerTimelineStep(title: String, isCompleted: Boolean, isLast: Boolean, isActive: Boolean, isRejected: Boolean) {
    val circleColor = when {
        isRejected -> Color(0xFFD32F2F)
        isActive && title != "Entregado" -> Color(0xFF1976D2)
        isActive && title == "Entregado" -> Color(0xFF4CAF50)
        isCompleted -> Color.Black
        else -> Color(0xFFE0E0E0)
    }

    val textColor = when {
        isRejected -> Color(0xFFD32F2F)
        isActive && title != "Entregado" -> Color(0xFF1976D2)
        isActive && title == "Entregado" -> Color(0xFF4CAF50)
        isCompleted -> Color.Black
        else -> Color.Gray
    }

    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(40.dp)) {
            Box(
                modifier = Modifier.size(24.dp).clip(CircleShape).background(circleColor),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted || isRejected) {
                    val icon = if (isRejected) Icons.Default.Close else Icons.Default.Check
                    Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            if (!isLast) {
                val lineColor = if (isCompleted && !isActive && !isRejected) Color.Black else Color(0xFFE0E0E0)
                Box(modifier = Modifier.width(2.dp).weight(1f).background(lineColor))
            }
        }
        Column(modifier = Modifier.weight(1f).padding(bottom = 32.dp)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = if (isCompleted || isRejected) FontWeight.Bold else FontWeight.Normal,
                color = textColor,
                modifier = Modifier.offset(y = 2.dp)
            )
        }
    }
}

// COMPONENTE: CAJITA DE TEXTOS
@Composable
private fun CustomerTimelineDetailRow(label: String, value: String, valueColor: Color = Color.Black) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}