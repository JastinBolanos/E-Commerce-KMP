package com.remedioz.natura.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.remedioz.natura.domain.model.Product
import com.remedioz.natura.domain.model.CartManager

@Composable
fun ProductCard(
    product: Product,
    showCartIcon: Boolean = true,
    modifier: Modifier = Modifier
) {
    val name = product.name
    val price = product.price

    // --- PERSISTENCIA ---
    val cartItems by CartManager.cartItems.collectAsState()
    val isInCart = cartItems.any { it.product.id == product.id }

    var expanded by remember { mutableStateOf(false) }
    var quantity by remember { mutableStateOf(1) }
    var showDetails by remember { mutableStateOf(false) }

    // CONTENEDOR PRINCIPAL
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE5E5E5), RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .animateContentSize()
    ) {
        // --- 1. IMAGEN ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.9f)
                .background(Color(0xFFEBEBEB))
        ) {
            // AÑADIMOS COIL PARA MOSTRAR LA FOTO
            if (product.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // --- BOTÓN DE CARRITO ---
            if (showCartIcon) {
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable {
                            if (isInCart) {
                                CartManager.removeProduct(productId = product.id)
                            } else {
                                CartManager.addProduct(product = product, quantity = quantity)
                            }
                        }
                        .background(if (isInCart) Color(0xFFFF5252) else Color.White, CircleShape)
                        .border(
                            width = if (isInCart) 0.dp else 1.dp,
                            color = Color.LightGray.copy(alpha = 0.5f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = "Añadir al carrito",
                        tint = if (isInCart) Color.White else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // --- 2. CONTENIDO (Textos y controles) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Fila de Título y Flecha
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                    Text(text = "S/ $price", fontSize = 14.sp, color = Color.Gray)
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expandir",
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
            }

            // --- 3. ACORDEÓN OCULTO ---
            if (expanded) {
                Spacer(modifier = Modifier.height(20.dp))

                // Fila: Detalles + Botón Ver
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Detalles", fontSize = 16.sp, color = Color(0xFF333333), modifier = Modifier.weight(1f))

                    // 2. botón "Ver"
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFF67B2C5), Color(0xFF5B78A5))))
                            .clickable { showDetails = true }
                            .padding(horizontal = 20.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Ver", color = Color.White, fontSize = 14.sp)
                    }

                    if (showDetails) {
                        ProductDetailDialog(
                            product = product,
                            onDismiss = { showDetails = false }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Precio: S/ $price", fontSize = 15.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(16.dp))

                // Selector de Cantidad [ - | 1 | + ]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón Menos
                    Box(
                        modifier = Modifier.weight(1f).fillMaxHeight().clickable { if (quantity > 1) quantity-- },
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Default.Remove, contentDescription = "-", tint = Color.Black) }

                    // Línea
                    Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color(0xFFCCCCCC)))

                    // Número
                    Box(modifier = Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                        Text(text = quantity.toString(), fontSize = 18.sp, color = Color.Black)
                    }

                    // Línea
                    Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color(0xFFCCCCCC)))

                    // Botón Más
                    Box(
                        modifier = Modifier.weight(1f).fillMaxHeight().clickable { quantity++ },
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Default.Add, contentDescription = "+", tint = Color.Black) }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Botón Comprar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.horizontalGradient(listOf(Color(0xFF4CB8FF), Color(0xFFFF7A8A))))
                        .clickable {
                            CartManager.addProduct(product = product, quantity = quantity)
                            println("Comprar $quantity $name")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Comprar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}