package com.remedioz.natura.presentation.features.admin

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
import org.jetbrains.compose.resources.Font
import remedioznatura_kmp.composeapp.generated.resources.Res
import remedioznatura_kmp.composeapp.generated.resources.imperial_script

// --- DATOS FALSOS (DEMO) ---
data class FakeOrder(
    val id: String,
    val productName: String,
    val unitPrice: String,
    val quantity: String,
    val totalCost: String,
    val status: String,
    val imageUrl: String
)

val demoOrders = listOf(
    FakeOrder("1", "StemRenu", "S/ 350", "2", "S/ 700", "Pendiente", "https://picsum.photos/seed/natura1/200/200"),
    FakeOrder("2", "Colágeno Premium", "S/ 120", "1", "S/ 120", "Pendiente", "https://picsum.photos/seed/natura2/200/200"),
    FakeOrder("3", "Kit Bienestar", "S/ 450", "3", "S/ 1,350", "Pendiente", "https://picsum.photos/seed/natura3/200/200")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    onBackClick: () -> Unit,
    onConfirmClick: (String) -> Unit
) {
    val imperialFont = FontFamily(Font(Res.font.imperial_script))
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Pendientes De Confirmacion", "Estado de Envios")

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
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = Color.Black,
                            height = 2.dp
                        )
                    },
                    divider = { HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp) }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 13.sp,
                                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTabIndex == index) Color.Black else Color.DarkGray
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        // --- LISTA DE PEDIDOS ---
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            items(demoOrders) { order ->
                OrderCardDemo(order = order, onConfirmClick = onConfirmClick)
                HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            }
        }
    }
}

// --- COMPONENTE: TARJETA DE PEDIDO ---
@Composable
fun OrderCardDemo(order: FakeOrder, onConfirmClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Columna Izquierda: Imagen y Textos del Producto
        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F0F0))
            ) {
                AsyncImage(
                    model = order.imageUrl,
                    contentDescription = order.productName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = order.productName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = order.unitPrice, fontSize = 12.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Columna Derecha: Estadísticas y Botones
        Column(modifier = Modifier.weight(1.5f)) {
            // Las 3 cajitas con borde
            OutlinedStatRow(label = "Cantidad:", value = order.quantity)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedStatRow(label = "Costos Totales:", value = order.totalCost)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedStatRow(label = "Estado:", value = order.status)

            Spacer(modifier = Modifier.height(16.dp))

            // Fila inferior: Ver Detalles y Botón
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ver Detalles",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.clickable { /* TODO: Mostrar detalles extra si se requiere */ }
                )

                Button(
                    onClick = { onConfirmClick(order.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9D9D9)),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Ir a Confirmar", color = Color.Black, fontSize = 12.sp)
                }
            }
        }
    }
}

// --- COMPONENTE: CAJITA DE ESTADÍSTICA CON BORDE ---
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