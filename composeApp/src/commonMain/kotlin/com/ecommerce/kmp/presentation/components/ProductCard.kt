package com.ecommerce.kmp.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecommerce.kmp.domain.model.Product
import com.ecommerce.kmp.presentation.state.CartManager
import e_commercekmp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

/**
 * ============================================================================
 * 🛍️ INTERACTIVE PRODUCT CARD COMPONENT
 * ============================================================================
 * * @description
 * This is the primary product display unit. It functions as a reactive accordion,
 * housing an expandable UI that reveals quantity selectors and purchasing calls
 * to action. It inherently observes the global `CartManager` state to reflect
 * real-time cart inclusion status via the quick-add floating action button.
 * * Key Compose UI/UX Features implemented:
 * - Layout Animation: Utilizes `animateContentSize()` to smoothly tween the
 * card boundaries during accordion expand/collapse events.
 * - Multi-Context Reusability: Respects the `isAdminView` flag to conditionally
 * hide transactional controls (Buy/Cart logic) when rendered within the
 * Administrator dashboard.
 * - Resource Mapping Factory: The `getProductImagePainter` acts as a local
 * fallback mechanism, mapping string identifiers to native drawable resources.
 * * 🔌 NOTE FOR BACKEND / ASSETS TEAM:
 * In this offline-first showcase, `getProductImagePainter` binds hardcoded
 * string IDs to local Kotlin Multiplatform resources. When migrating to a live
 * Database, replace this function call with an `AsyncImage` (e.g., Coil or Glide)
 * that loads the raw CDN URL provided by the `product.imageUrl` field.
 * * @layer Presentation / Components
 * ============================================================================
 */

@Composable
fun ProductCard(
    product: Product,
    showCartIcon: Boolean = true,
    isAdminView: Boolean = false,
    modifier: Modifier = Modifier,
    onBuyNowClick: (Int) -> Unit = {}
) {
    val name = product.name
    val price = product.price

    // --- STATE ---
    val cartItems by CartManager.cartItems.collectAsState()
    val isInCart = cartItems.any { it.product.id == product.id }

    var expanded by remember { mutableStateOf(false) }
    var quantity by remember { mutableStateOf(1) }
    var showDetails by remember { mutableStateOf(false) }

    // --- MAIN CONTAINER ---
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE5E5E5), RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .animateContentSize()
    ) {
        // --- 1. IMAGE ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.9f)
                .background(Color(0xFFEBEBEB))
        ) {
            if (product.imageUrl.isNotEmpty()) {
                Image(
                    painter = getProductImagePainter(product.imageUrl),
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // --- CART BUTTON ---
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
                        contentDescription = "Add to cart",
                        tint = if (isInCart) Color.White else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // --- 2. CONTENT (Texts and controls) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            // --- Title and Arrow Row ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                    Text(text = "\$${product.price.format(2)}", fontSize = 14.sp, color = Color.Gray)
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
            }

            // --- 3. HIDDEN ACCORDION ---
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))

                // --- Details + View Button ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Details", fontSize = 16.sp, color = Color(0xFF333333), modifier = Modifier.weight(1f))

                    // --- "View" button ---
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFF67B2C5), Color(0xFF5B78A5))))
                            .clickable { showDetails = true }
                            .padding(horizontal = 20.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("View", color = Color.White, fontSize = 14.sp)
                    }

                    if (showDetails) {
                        ProductDetailDialog(
                            product = product,
                            imagePainter = getProductImagePainter(product.imageUrl),
                            onDismiss = { showDetails = false }
                        )
                    }
                }

                if (!isAdminView) {
                    Spacer(modifier = Modifier.height(16.dp))

                    val calculatedTotal = product.price * quantity

                    Text(
                        text = if (quantity > 1) "Subtotal: \$${calculatedTotal.format(2)}" else "Price: \$${calculatedTotal.format(2)}",
                        fontSize = 15.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Quantity Selector [ - | 1 | + ] ---
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // --- Minus Button ---
                        Box(
                            modifier = Modifier.weight(1f).fillMaxHeight().clickable { if (quantity > 1) quantity-- },
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Default.Remove, contentDescription = "-", tint = Color.Black) }

                        // --- Line ---
                        Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color(0xFFCCCCCC)))

                        // --- Number ---
                        Box(modifier = Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                            Text(text = quantity.toString(), fontSize = 18.sp, color = Color.Black)
                        }

                        // --- Line ---
                        Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color(0xFFCCCCCC)))

                        // --- Plus Button ---
                        Box(
                            modifier = Modifier.weight(1f).fillMaxHeight().clickable { quantity++ },
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Default.Add, contentDescription = "+", tint = Color.Black) }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // --- Buy Button ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFF4CB8FF), Color(0xFFFF7A8A))))
                            .clickable {
                                onBuyNowClick(quantity)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Buy", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
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

@Composable
fun getProductImagePainter(imageUrl: String): Painter {
    val imageRes = when (imageUrl) {
        // --- CATEGORY 1: SKIN ---
        "img_serum_pink_peptide" -> Res.drawable.img_serum_pink_peptide
        "img_crema_dolkong" -> Res.drawable.img_crema_dolkong
        "img_tonico_purificante" -> Res.drawable.img_tonico_purificante
        "img_crema_noche" -> Res.drawable.img_crema_noche
        "img_jabon_manzanilla" -> Res.drawable.img_jabon_manzanilla
        "img_jabon_arcilla" -> Res.drawable.img_jabon_arcilla

        // --- CATEGORY 2: BEAUTY ---
        "img_balsamo_citrico" -> Res.drawable.img_balsamo_citrico
        "img_perfume_rojo" -> Res.drawable.img_perfume_rojo
        "img_tinte_seda" -> Res.drawable.img_tinte_seda
        "img_perfume_azul" -> Res.drawable.img_perfume_azul
        "img_rubor_liquido" -> Res.drawable.img_rubor_liquido
        "img_balsamo_herbal" -> Res.drawable.img_balsamo_herbal

        // --- CATEGORY 3: CARE ---
        "img_aceite_dorado" -> Res.drawable.img_aceite_dorado
        "img_mantequilla_caramelo" -> Res.drawable.img_mantequilla_caramelo
        "img_aceite_macadamia" -> Res.drawable.img_aceite_macadamia
        "img_shampoo_solido" -> Res.drawable.img_shampoo_solido
        "img_exfoliante_corporal" -> Res.drawable.img_exfoliante_corporal
        "img_jabon_avena" -> Res.drawable.img_jabon_avena

        // --- CATEGORY 4: TREATMENTS ---
        "img_esencia_lechosa" -> Res.drawable.img_esencia_lechosa
        "img_mascarilla_citrica" -> Res.drawable.img_mascarilla_citrica
        "img_tratamiento_capilar" -> Res.drawable.img_tratamiento_capilar
        "img_gel_vitamina_c" -> Res.drawable.img_gel_vitamina_c
        "img_tratamiento_cuero_cabelludo" -> Res.drawable.img_tratamiento_cuero_cabelludo
        "img_mascarilla_miel" -> Res.drawable.img_mascarilla_miel

        // --- FALLBACK (In case an image is not found) ---
        else -> Res.drawable.img_tinte_seda
    }
    return painterResource(imageRes)
}