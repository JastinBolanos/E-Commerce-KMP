package com.ecommerce.kmp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.ecommerce.kmp.domain.model.Product

@Composable
fun ProductDetailDialog(
    product: Product,
    imagePainter: Painter,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(24.dp)),
            color = Color.White
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // --- Botón Cerrar (X) Flotante ---
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(50))
                        .zIndex(1f)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.Black)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // --- 1. IMAGEN GRANDE ---
                    Image(
                        painter = imagePainter,
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp)
                            .background(Color(0xFFEBEBEB))
                    )

                    // --- 2. DETALLES Y DESCRIPCIÓN ---
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = product.category.uppercase(),
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = product.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            lineHeight = 28.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "S/ ${product.price}",
                            fontSize = 20.sp,
                            color = Color(0xFF67B2C5),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Acerca de este producto",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = product.description.ifEmpty { "Descripción no disponible para este producto." },
                            fontSize = 15.sp,
                            color = Color.DarkGray,
                            lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}