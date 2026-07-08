package com.ecommerce.kmp.presentation.features.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.ecommerce.kmp.domain.model.CartItem
import com.ecommerce.kmp.presentation.components.ProductDetailDialog
import com.ecommerce.kmp.presentation.components.getKitImagePainter
import com.ecommerce.kmp.presentation.state.CartManager
import e_commercekmp.composeapp.generated.resources.Res
import e_commercekmp.composeapp.generated.resources.imperial_script
import org.jetbrains.compose.resources.Font

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBackClick: () -> Unit,
    onProceedToCheckoutClick: () -> Unit
) {
    val cartItems by CartManager.cartItems.collectAsState()

    // Lógica Matemática Optimizada
    val totalAmount = cartItems.sumOf { item ->
        item.product.price * item.quantity
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Carrito de Compras",
                            fontFamily = FontFamily(Font(Res.font.imperial_script)),
                            fontWeight = FontWeight.Normal,
                            fontSize = 38.sp,
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
                HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
            }
        },
        bottomBar = {
            // --- FOOTER: TOTAL Y BOTÓN COMPRAR TODO ---
            if (cartItems.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(24.dp)
                ) {
                    Text("Total: S/ ${totalAmount.format(2)}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFF6B4BFF), Color(0xFF00E676))))
                            .clickable { onProceedToCheckoutClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Comprar Todo", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    ) { paddingValues ->

        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío", fontSize = 18.sp, color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
            ) {
                items(cartItems) { item ->
                    CartItemRow(cartItem = item)
                    HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                }
            }
        }
    }
}

// --- COMPONENTE INTERNO: LA TARJETA DEL PRODUCTO EN EL CARRITO ---
@Composable
fun CartItemRow(cartItem: CartItem) {

    val product = cartItem.product
    var showDetails by remember { mutableStateOf(false) }

    // Cálculo de subtotal directo
    val subtotal = product.price * cartItem.quantity

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .width(130.dp)
                .aspectRatio(0.9f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFEBEBEB))
        ) {
            if (product.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 2. Columna de detalles a la derecha
        Column(modifier = Modifier.weight(1f)) {
            // Título y botón de eliminar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        product.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text("S/ ${product.price.format(2)}", fontSize = 12.sp, color = Color.Gray)
                }

                // Botón de carrito
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF5252))
                        .clickable { CartManager.removeProduct(product.id) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = "Eliminar",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Fila "Detalles" y botón "Ver"
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Detalles", fontSize = 13.sp, color = Color(0xFF333333))
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF67B2C5),
                                    Color(0xFF5B78A5)
                                )
                            )
                        )
                        .clickable { showDetails = true }
                        .padding(horizontal = 12.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ver", color = Color.White, fontSize = 10.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Subtotal: S/ ${subtotal.format(2)}", fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .width(120.dp)
                    .height(32.dp)
                    .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(6.dp))
                    .clip(RoundedCornerShape(6.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight()
                        .clickable {
                            if (cartItem.quantity > 1) CartManager.addProduct(product, -1)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "-",
                        tint = Color.Black,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color(0xFFCCCCCC)))
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = cartItem.quantity.toString(), fontSize = 14.sp, color = Color.Black)
                }
                Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color(0xFFCCCCCC)))
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight()
                        .clickable {
                            CartManager.addProduct(product, 1)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "+",
                        tint = Color.Black,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón Comprar
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF4CB8FF),
                                Color(0xFFFF7A8A)
                            )
                        )
                    )
                    .clickable { println("Comprar directo: ${cartItem.quantity} ${product.name}") },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Comprar",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            if (showDetails) {
                ProductDetailDialog(
                    product = product,
                    imagePainter = getKitImagePainter(product.imageUrl),
                    onDismiss = { showDetails = false }
                )
            }
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