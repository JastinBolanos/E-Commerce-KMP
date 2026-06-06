package com.remedioz.natura.presentation.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import remedioznatura_kmp.composeapp.generated.resources.Res
import remedioznatura_kmp.composeapp.generated.resources.imperial_script

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerTimelineScreen(
    orderId: String,
    currentStatus: String,
    onBackClick: () -> Unit
) {
    val imperialFont = FontFamily(Font(Res.font.imperial_script))

    // EL FLUJO DEL CLIENTE (Agregamos "Pendiente" para que sepa que estamos revisando su pago)
    val statusFlow = listOf("Pendiente", "Aprobado", "Preparando Paquete", "En Camino", "Entregado")

    val currentIndex = statusFlow.indexOfFirst { it.equals(currentStatus, ignoreCase = true) }.takeIf { it >= 0 } ?: 0

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
                    CustomerTimelineDetailRow("ID Pedido:", orderId.takeLast(6).uppercase())
                    Spacer(modifier = Modifier.height(8.dp))

                    val statusColor = if (currentStatus.equals("Entregado", ignoreCase = true)) Color(0xFF4CAF50) else Color(0xFF1976D2)
                    CustomerTimelineDetailRow("Estado Actual:", currentStatus, valueColor = statusColor)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // --- 2. LA LÍNEA DE TIEMPO VISUAL ---
            Text("Progreso del Envío", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(24.dp))

            statusFlow.forEachIndexed { index, stepName ->
                val isCompleted = index <= currentIndex
                val isLast = index == statusFlow.lastIndex
                val isActive = index == currentIndex

                CustomerTimelineStep(
                    title = stepName,
                    isCompleted = isCompleted,
                    isLast = isLast,
                    isActive = isActive
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // --- 3. MENSAJE FINAL AMIGABLE ---
            if (currentStatus.equals("Entregado", ignoreCase = true)) {
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp)).padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("¡Gracias por tu compra! Disfruta tus productos Natura.", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
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
private fun CustomerTimelineStep(title: String, isCompleted: Boolean, isLast: Boolean, isActive: Boolean) {
    val circleColor = when {
        isActive && title != "Entregado" -> Color(0xFF1976D2)
        isActive && title == "Entregado" -> Color(0xFF4CAF50)
        isCompleted -> Color.Black
        else -> Color(0xFFE0E0E0)
    }

    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(40.dp)) {
            Box(
                modifier = Modifier.size(24.dp).clip(CircleShape).background(circleColor),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            if (!isLast) {
                val lineColor = if (isCompleted && !isActive) Color.Black else Color(0xFFE0E0E0)
                Box(modifier = Modifier.width(2.dp).weight(1f).background(lineColor))
            }
        }
        Column(modifier = Modifier.weight(1f).padding(bottom = 32.dp)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = if (isCompleted) FontWeight.Bold else FontWeight.Normal,
                color = if (isActive && title != "Entregado") Color(0xFF1976D2) else if (isActive && title == "Entregado") Color(0xFF4CAF50) else if (isCompleted) Color.Black else Color.Gray,
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