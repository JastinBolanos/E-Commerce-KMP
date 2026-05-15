package com.remedioz.natura.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.remedioz.natura.domain.model.Product

@Composable
fun ProductDetailDialog(
    product: Product,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(32.dp)),
            color = Color.White
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Botón Cerrar (X) arriba a la derecha
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).zIndex(1f)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.Gray)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 24.dp, end = 24.dp, bottom = 24.dp, top = 56.dp)
                ) {
                    // --- CUADRÍCULA DE IMÁGENES (Diseño exacto) ---
                    Row(
                        modifier = Modifier.fillMaxWidth().height(280.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Imagen grande izquierda
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFC4C4C4))
                        )

                        // Dos imágenes pequeñas Derecha
                        Column(
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFFC4C4C4)))
                            Box(modifier = Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFFC4C4C4)))
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- DESCRIPCIÓN ---
                    Text(
                        text = product.description.ifEmpty { "Descripción no disponible para este producto." },
                        fontSize = 18.sp,
                        color = Color.Black,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}