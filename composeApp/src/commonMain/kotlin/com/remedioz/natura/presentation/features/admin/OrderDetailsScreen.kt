package com.remedioz.natura.presentation.features.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.Font
import remedioznatura_kmp.composeapp.generated.resources.Res
import remedioznatura_kmp.composeapp.generated.resources.imperial_script

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
    orderId: String,
    voucherUrl: String,
    customerName: String,
    totalAmount: String,
    status: String,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onApproveClick: (String) -> Unit,
    onRejectClick: (String) -> Unit
) {
    val imperialFont = FontFamily(Font(Res.font.imperial_script))

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text("Veredicto de Pago", fontFamily = imperialFont, fontSize = 36.sp, color = Color.Black)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(24.dp)
        ) {
            // --- 1. SECCIÓN DEL VOUCHER ---
            Text("Comprobante del Cliente", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF9F9F9))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (voucherUrl.isNotEmpty()) {
                    AsyncImage(
                        model = voucherUrl,
                        contentDescription = "Voucher",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    CircularProgressIndicator(color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- 2. RESUMEN DEL PEDIDO ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DetailRow("ID Pedido:", orderId)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailRow("Cliente:", customerName)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailRow("Monto Total:", totalAmount)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailRow("Estado Actual:", status, valueColor = Color(0xFFD32F2F))
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // --- 3. CONTROLES DE VEREDICTO ---
            if (status.equals("Pendiente", ignoreCase = true)) {
                Button(
                    onClick = { onApproveClick(orderId) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    else Text("Aprobar Pago (Iniciar Envío)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { onRejectClick(orderId) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isLoading,
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    if (isLoading) CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                    else Text("Rechazar (Voucher Inválido)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFEBEBEB), RoundedCornerShape(12.dp)).padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("Este pedido ya fue procesado.", color = Color.DarkGray, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, valueColor: Color = Color.Black) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}