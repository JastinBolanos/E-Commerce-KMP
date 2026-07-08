package com.ecommerce.kmp.presentation.features.profile

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import com.ecommerce.kmp.domain.model.Order
import com.ecommerce.kmp.presentation.components.getKitImagePainter
import com.ecommerce.kmp.presentation.components.getProductImagePainter
import e_commercekmp.composeapp.generated.resources.Res
import e_commercekmp.composeapp.generated.resources.img_perfil
import e_commercekmp.composeapp.generated.resources.imperial_script

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerProfileScreen(
    userName: String?,
    userEmail: String?,
    userPhotoUrl: String?,
    orders: List<Order>,
    onBackClick: () -> Unit,
    onTrackOrderClick: (Order) -> Unit
) {
    val imperialFont = FontFamily(Font(Res.font.imperial_script))

    // MOTOR DE ANIMACIÓN PARA EL BORDE DEL PERFIL
    val infiniteTransition = rememberInfiniteTransition(label = "profile_border_animation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

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
            // --- 1. CABECERA DEL PERFIL ---
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // CÍRCULO DE FOTO CON BORDE ANIMADO
                    Box(
                        modifier = Modifier
                            .size(106.dp)
                            .clip(CircleShape)
                            .drawBehind {
                                rotate(angle) {
                                    drawCircle(
                                        brush = Brush.sweepGradient(
                                            colors = listOf(
                                                Color(0xFFE0E0E0),
                                                Color(0xFFE0E0E0),
                                                Color(0xFF9E9E9E),
                                                Color(0xFF424242),
                                                Color(0xFF9E9E9E),
                                                Color(0xFFE0E0E0),
                                                Color(0xFFE0E0E0)
                                            )
                                        ),
                                        radius = size.width
                                    )
                                }
                            }
                            .padding(3.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF0F0F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.img_perfil),
                            contentDescription = "Foto de perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    Column {
                        Text("Datos Personales", fontSize = 16.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(userName ?: "Laura Veracruz", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(userEmail ?: "laura.official@gmail.com", fontSize = 14.sp, color = Color.Gray)
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
    val firstProductPrice = firstProduct?.price?.format(2) ?: "0.00"

    // LÓGICA DE TRADUCCIÓN DE IMÁGENES
    val isKit = firstProduct?.category.equals("Kits", ignoreCase = true)
    val imagePainter = if (isKit) {
        getKitImagePainter(firstProductImage)
    } else {
        getProductImagePainter(firstProductImage)
    }

    val totalItems = order.items.sumOf { it.quantity }
    val isDelivered = order.status.equals("Entregado", ignoreCase = true)
    val buttonColor = if (isDelivered) Color(0xFF97DAFD) else Color(0xFFD3D3D3)
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
                Image(
                    painter = imagePainter,
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
            CustomerOutlinedStatRow(label = "Costos Totales:", value = "S/ ${order.totalAmount.format(2)}")

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

private fun Double.format(digits: Int): String {
    val rounded = (this * 100).toLong() / 100.0
    val parts = rounded.toString().split(".")
    val whole = parts[0]
    val fraction = if (parts.size > 1) parts[1] else "0"
    return "$whole.${fraction.padEnd(digits, '0')}"
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