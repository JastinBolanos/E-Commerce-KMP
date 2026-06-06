package com.remedioz.natura.presentation.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.remedioz.natura.domain.model.Order
import org.jetbrains.compose.resources.Font
import remedioznatura_kmp.composeapp.generated.resources.Res
import remedioznatura_kmp.composeapp.generated.resources.imperial_script

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerProfileScreen(
    orders: List<Order>,
    onBackClick: () -> Unit,
    onTrackOrderClick: (Order) -> Unit
) {
    val imperialFont = FontFamily(Font(Res.font.imperial_script))

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text("Perfil", fontFamily = imperialFont, fontSize = 36.sp, color = Color.Black)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // --- 1. CABECERA DEL PERFIL (DATOS FALSOS / MOCK) ---
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Círculo de foto de perfil
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFAAAAAA))
                    )

                    Spacer(modifier = Modifier.width(24.dp))

                    Column {
                        Text("Datos Personales", fontSize = 16.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Cliente de App", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text("cliente@prueba.com", fontSize = 14.sp, color = Color.Gray)
                    }
                }

                HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)

                // --- TÍTULO DE SECCIÓN ---
                Text(
                    text = "Tus Pedidos",
                    fontSize = 30.sp,
                    fontFamily = imperialFont,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
                HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            }

            // --- 2. LISTA DE PEDIDOS REALES ---
            items(orders) { order ->
                CustomerOrderCard(order = order, onTrackClick = { onTrackOrderClick(order) })
                HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            }

            if (orders.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Aún no tienes pedidos.", color = Color.Gray)
                    }
                }
            }
        }
    }
}

// --- COMPONENTE: TARJETA DE PEDIDO DEL CLIENTE ---
@Composable
private fun CustomerOrderCard(order: Order, onTrackClick: () -> Unit) {
    val firstProduct = order.items.firstOrNull()?.product
    val firstProductImage = firstProduct?.imageUrl ?: ""
    val firstProductName = firstProduct?.name ?: "Producto Natura"
    val firstProductPrice = firstProduct?.price?.toString() ?: "0.0"
    val totalItems = order.items.sumOf { it.quantity }
    val isDelivered = order.status.equals("Entregado", ignoreCase = true)
    val buttonColor = if (isDelivered) Color(0xFF00FF00) else Color(0xFFD3D3D3)
    val textColor = Color.Black

    Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // Columna Izquierda: Imagen y Textos
        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F0F0))
            ) {
                AsyncImage(
                    model = firstProductImage,
                    contentDescription = "Producto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = firstProductName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = "S/ $firstProductPrice", fontSize = 12.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Columna Derecha: Estadísticas y Botón de Estado
        Column(modifier = Modifier.weight(1.5f)) {
            CustomerOutlinedStatRow(label = "Cantidad:", value = totalItems.toString())
            Spacer(modifier = Modifier.height(6.dp))
            CustomerOutlinedStatRow(label = "Costos Totales:", value = "S/ ${order.totalAmount}")
            Spacer(modifier = Modifier.height(6.dp))
            CustomerOutlinedStatRow(label = "Estado:", value = order.status)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ver Detalles",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.clickable { onTrackClick() }
                )

                // Botón de Estado
                Button(
                    onClick = onTrackClick,
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(text = order.status, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Cajita de estadísticas
@Composable
private fun CustomerOutlinedStatRow(label: String, value: String) {
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