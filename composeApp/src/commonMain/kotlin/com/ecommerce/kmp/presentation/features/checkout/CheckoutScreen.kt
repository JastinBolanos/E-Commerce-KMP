package com.ecommerce.kmp.presentation.features.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.launch

/**
 * Pantalla de finalización de compra y recaudo de pagos (Checkout).
 * Permite al usuario visualizar el monto final y adjuntar su comprobante de pago (Voucher).
 * Se comunica de forma ascendente (State Hoisting) emitiendo el voucher en formato ByteArray.
 *
 * @param totalAmount Monto total calculado desde el carrito.
 * @param onBackClick Navegación hacia atrás (regresar al carrito).
 * @param onConfirmOrder Dispara el proceso de guardado en la nube enviando la foto del voucher.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    totalAmount: Double,
    isLoading: Boolean,
    qrUrl: String,
    phoneNumber: String,
    onBackClick: () -> Unit,
    onConfirmOrder: (ByteArray) -> Unit
) {
    var selectedVoucherBytes by remember { mutableStateOf<ByteArray?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberFilePickerLauncher(
        type = PickerType.Image,
        mode = PickerMode.Single,
        title = "Selecciona tu comprobante de pago"
    ) { platformFile ->
        if (platformFile != null) {
            coroutineScope.launch {
                selectedVoucherBytes = platformFile.readBytes()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PAGO", fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // --- 1. RESUMEN DE MONTO ---
            Text("Total a Pagar", fontSize = 16.sp, color = Color.Gray)
            Text(
                text = "S/ ${totalAmount.format(2)}",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- 2. INSTRUCCIONES Y DATOS BANCARIOS ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Escanea o transfiere vía Yape/Plin al:", color = Color.DarkGray, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    // EL CUADRADO DEL QR
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEBEBEB)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (qrUrl.isNotEmpty()) {
                            AsyncImage(
                                model = qrUrl,
                                contentDescription = "QR Oficial de Pago",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.QrCodeScanner, contentDescription = "QR de Pago", tint = Color.Gray, modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("QR dinámico", color = Color.Gray, fontSize = 10.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = phoneNumber, fontSize = 24.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Text("Johana Quispe Ortiz", color = Color.Gray, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            // --- 3. SUBIDA DE VOUCHER (FileKit) ---
            Text(
                "Adjunta tu comprobante",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF0F0F0))
                    .border(2.dp, if (selectedVoucherBytes == null) Color.LightGray else Color(0xFF6B4BFF), RoundedCornerShape(16.dp))
                    .clickable { launcher.launch() },
                contentAlignment = Alignment.Center
            ) {
                if (selectedVoucherBytes != null) {
                    AsyncImage(
                        model = selectedVoucherBytes,
                        contentDescription = "Voucher seleccionado",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Toca para subir tu foto", color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // --- 4. ACCIÓN FINAL ---
            Button(
                onClick = {
                    if (selectedVoucherBytes != null) {
                        onConfirmOrder(selectedVoucherBytes!!)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = selectedVoucherBytes != null,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    disabledContainerColor = Color.LightGray
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text("Confirmar Pago y Enviar Pedido", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

fun Double.format(digits: Int): String {
    val rounded = (this * 100).toLong() / 100.0
    val parts = rounded.toString().split(".")
    val whole = parts[0]
    val fraction = if (parts.size > 1) parts[1] else "0"
    return "$whole.${fraction.padEnd(digits, '0')}"
}