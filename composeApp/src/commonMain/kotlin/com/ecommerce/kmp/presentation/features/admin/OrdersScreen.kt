package com.ecommerce.kmp.presentation.features.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
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
import com.ecommerce.kmp.domain.model.Order
import org.jetbrains.compose.resources.Font
import e_commercekmp.composeapp.generated.resources.Res
import e_commercekmp.composeapp.generated.resources.imperial_script

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    orders: List<Order>,
    selectedTab: Int,
    onTabChange: (Int) -> Unit,
    onBackClick: () -> Unit,
    onConfirmClick: (Order) -> Unit
) {
    val imperialFont = FontFamily(Font(Res.font.imperial_script))
    val tabs = listOf("Pendientes De Confirmacion", "Estado de Envios")

    val displayedOrders = if (selectedTab == 0) {
        orders.filter { it.status.equals("Pendiente", ignoreCase = true) }
    } else {
        orders.filter { !it.status.equals("Pendiente", ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Pedidos",
                            fontFamily = imperialFont,
                            fontSize = 36.sp,
                            color = Color.Black
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Volver", tint = Color.Black)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )

                // --- TABS (Pestañas) ---
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Color.Black,
                            height = 2.dp
                        )
                    },
                    divider = { HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp) }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { onTabChange(index) },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 13.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index) Color.Black else Color.DarkGray
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        // --- LISTA DE PEDIDOS REALES ---
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            items(displayedOrders) { order ->
                OrderCardReal(order = order, onConfirmClick = { onConfirmClick(order) })
                HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            }
            if (displayedOrders.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No hay pedidos en esta sección.", color = Color.Gray)
                    }
                }
            }
        }
    }
}

// --- COMPONENTE: TARJETA DE PEDIDO REAL ---
@Composable
fun OrderCardReal(order: Order, onConfirmClick: () -> Unit) {
    val firstProductImage = order.items.firstOrNull()?.product?.imageUrl ?: ""
    val totalItems = order.items.sumOf { it.quantity }
    val productSummary = order.items.joinToString(", ") { "${it.quantity}x ${it.product.name}" }

    val isPending = order.status.equals("Pendiente", ignoreCase = true)
    val isDelivered = order.status.equals("Entregado", ignoreCase = true)

    val buttonColor = when {
        isPending -> Color(0xFFD9D9D9)
        isDelivered -> Color(0xFF4CAF50)
        else -> Color.Black
    }

    val buttonText = when {
        isPending -> "Ir a Confirmar"
        isDelivered -> "Finalizado"
        else -> "Gestionar Envío"
    }

    Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // Columna Izquierda: Imagen del PRODUCTO
        Column(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF0F0F0))) {
                AsyncImage(
                    model = firstProductImage,
                    contentDescription = "Producto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Pedido: ${order.id.takeLast(4)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Columna Derecha: Estadísticas y Botón Inteligente
        Column(modifier = Modifier.weight(1.5f)) {
            // Resumen de productos comprados
            Text(
                text = productSummary,
                fontSize = 12.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Estadísticas
            OutlinedStatRow(label = "Cant:", value = "$totalItems prod.")
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedStatRow(label = "Total:", value = "S/ ${order.totalAmount}")
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedStatRow(label = "Estado:", value = order.status)

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Ver Detalles",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.clickable { })

                Button(
                    onClick = onConfirmClick,
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(
                        text = buttonText,
                        color = if (isPending) Color.Black else Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

// --- COMPONENTE: CAJITA DE ESTADÍSTICA ---
@Composable
fun OutlinedStatRow(label: String, value: String) {
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