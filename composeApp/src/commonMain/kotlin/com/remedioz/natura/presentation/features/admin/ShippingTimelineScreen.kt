package com.remedioz.natura.presentation.features.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import remedioznatura_kmp.composeapp.generated.resources.Res
import remedioznatura_kmp.composeapp.generated.resources.imperial_script

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingTimelineScreen(
    orderId: String,
    customerName: String,
    currentStatus: String,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onUpdateStatusClick: (String) -> Unit
) {
    val imperialFont = FontFamily(Font(Res.font.imperial_script))
    val statusFlow = listOf("Aprobado", "Preparando Paquete", "En Camino", "Entregado")
    val currentIndex = statusFlow.indexOfFirst { it.equals(currentStatus, ignoreCase = true) }.takeIf { it >= 0 } ?: 0
    var showConfirmDialog by remember { mutableStateOf(false) }
    var pendingNextStatus by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { Text("Estado del Envío", fontFamily = imperialFont, fontSize = 36.sp, color = Color.Black) },
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
                    TimelineDetailRow("ID Pedido:", orderId.takeLast(6).uppercase())
                    Spacer(modifier = Modifier.height(8.dp))
                    TimelineDetailRow("Cliente:", customerName)
                    Spacer(modifier = Modifier.height(8.dp))
                    TimelineDetailRow("Estado Actual:", currentStatus, valueColor = Color(0xFF1976D2))
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // --- 2. LA LÍNEA DE TIEMPO VERTICAL ---
            Text("Seguimiento", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(24.dp))

            statusFlow.forEachIndexed { index, stepName ->
                val isCompleted = index <= currentIndex
                val isLast = index == statusFlow.lastIndex

                TimelineStep(
                    title = stepName,
                    isCompleted = isCompleted,
                    isLast = isLast
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // --- 3. EL BOTÓN (Cambia según el estado) ---
            if (currentIndex < statusFlow.lastIndex) {
                val nextStatus = statusFlow[currentIndex + 1]

                Button(
                    onClick = {
                        pendingNextStatus = nextStatus
                        showConfirmDialog = true
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Pasar a: $nextStatus", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp)).border(1.dp, Color(0xFF4CAF50), RoundedCornerShape(12.dp)).padding(16.dp), contentAlignment = Alignment.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4CAF50))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("¡Pedido Entregado con Éxito!", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // CUADRO FLOTANTE DE CONFIRMACIÓN (Alerta)
        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                containerColor = Color.White,
                title = {
                    Text("Confirmar Cambio", fontWeight = FontWeight.Bold, color = Color.Black)
                },
                text = {
                    Text("¿Estás seguro que deseas avanzar este pedido al estado:\n\n\"$pendingNextStatus\"?\n\nEsta acción notificará al cliente.", color = Color.DarkGray)
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showConfirmDialog = false
                            onUpdateStatusClick(pendingNextStatus)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Sí, avanzar", color = Color.White)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showConfirmDialog = false },
                        border = BorderStroke(1.dp, Color.Black),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancelar", color = Color.Black)
                    }
                }
            )
        }
    }
}

// COMPONENTE: DIBUJA CADA PASO DE LA LÍNEA DE TIEMPO
@Composable
fun TimelineStep(title: String, isCompleted: Boolean, isLast: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        // Columna Izquierda (Dibujo de la bolita y la línea)
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(40.dp)) {
            Box(
                modifier = Modifier.size(24.dp).clip(CircleShape).background(if (isCompleted) Color.Black else Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }

            if (!isLast) {
                Box(modifier = Modifier.width(2.dp).weight(1f).background(if (isCompleted) Color.Black else Color(0xFFE0E0E0)))
            }
        }

        // Columna Derecha (Texto)
        Column(modifier = Modifier.weight(1f).padding(bottom = 32.dp)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = if (isCompleted) FontWeight.Bold else FontWeight.Normal,
                color = if (isCompleted) Color.Black else Color.Gray,
                modifier = Modifier.offset(y = 2.dp)
            )
        }
    }
}

@Composable
private fun TimelineDetailRow(label: String, value: String, valueColor: Color = Color.Black) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}