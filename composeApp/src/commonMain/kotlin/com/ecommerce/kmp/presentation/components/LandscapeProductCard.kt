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
 * 🎁 LANDSCAPE PRODUCT CARD (PROMOTIONAL KITS)
 * ============================================================================
 * * @description
 * This component is a horizontally-biased variant of the standard ProductCard.
 * It is specifically engineered to be rendered within horizontal carousels
 * (`LazyRow`), maintaining an optimal 1.6:1 aspect ratio for promotional
 * kits or bundle photography.
 * * Key Compose UI/UX Features implemented:
 * - Carousel Optimization: Enforces a fixed `280.dp` width, ensuring consistent
 * off-screen peeking for horizontal scroll discoverability.
 * - Reactive Accordion: Leverages `animateContentSize()` to expand vertically
 * without disrupting the horizontal bounds of its parent container.
 * - Context-Aware Action Buttons: Synchronizes seamlessly with the global
 * `CartManager` state and conditionally hides transactional UI layers when
 * accessed via the `isAdminView`.
 * * 🔌 NOTE FOR BACKEND / ASSETS TEAM:
 * The `getKitImagePainter` function handles local fallback mapping. When
 * transitioning to a live database, swap this local painter instantiation
 * with a remote asynchronous image loader (e.g., Coil `AsyncImage`) utilizing
 * the payload from `product.imageUrl`.
 * * @layer Presentation / Components
 * ============================================================================
 */

@Composable
fun LandscapeProductCard(
    product: Product,
    showCartIcon: Boolean = true,
    isAdminView: Boolean = false,
    modifier: Modifier = Modifier,
    onBuyNowClick: (Int) -> Unit = {}
) {
    val name = product.name
    val price = product.price

    // --- STATES ---
    val cartItems by CartManager.cartItems.collectAsState()
    val isInCart = cartItems.any { it.product.id == product.id }
    var expanded by remember { mutableStateOf(false) }
    var quantity by remember { mutableStateOf(1) }
    var showDetails by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .width(280.dp)
            .border(1.dp, Color(0xFFE5E5E5), RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .animateContentSize()
    ) {
        // --- Main Image Box ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.6f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFD6D6D6))
        ) {
            if (product.imageUrl.isNotEmpty()) {
                // Kits Translator
                Image(
                    painter = getKitImagePainter(product.imageUrl),
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // --- CART BUTTON ---
            if (!isAdminView && showCartIcon) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd)
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable {
                            if (isInCart) {
                                CartManager.removeProduct(product.id)
                            } else {
                                CartManager.addProduct(product, quantity)
                            }
                        }
                        .background(
                            color = if (isInCart) Color(0xFFFF5252) else Color.White,
                            shape = CircleShape
                        )
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
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

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
                    Text(
                        text = name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(text = "\$${product.price.format(2)}", fontSize = 14.sp, color = Color.Gray)
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = Color.Black
                )
            }

            // EXPANDABLE CONTENT
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))

                // --- Details Row + View Button ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Details",
                        fontSize = 14.sp,
                        color = Color(0xFF333333),
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFF67B2C5), Color(0xFF5B78A5))))
                            .clickable { showDetails = true }
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("View", color = Color.White, fontSize = 12.sp)
                    }
                }

                if (!isAdminView) {
                    Spacer(modifier = Modifier.height(12.dp))

                    val calculatedTotal = product.price * quantity

                    Text(
                        text = if (quantity > 1) "Subtotal: \$${calculatedTotal.format(2)}" else "Price: \$${calculatedTotal.format(2)}",
                        fontSize = 15.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // --- Quantity Selector ---
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(1f).fillMaxHeight()
                                .clickable { if (quantity > 1) quantity-- },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "-", tint = Color.Black, modifier = Modifier.size(16.dp))
                        }
                        Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color(0xFFCCCCCC)))
                        Box(modifier = Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                            Text(text = quantity.toString(), fontSize = 16.sp, color = Color.Black)
                        }
                        Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color(0xFFCCCCCC)))
                        Box(
                            modifier = Modifier.weight(1f).fillMaxHeight().clickable { quantity++ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "+", tint = Color.Black, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Buy Button ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFF4CB8FF), Color(0xFFFF7A8A))))
                            .clickable {
                                onBuyNowClick(quantity)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Buy", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }

    if (showDetails) {
        ProductDetailDialog(
            product = product,
            imagePainter = getKitImagePainter(product.imageUrl),
            onDismiss = { showDetails = false }
        )
    }
}

// Translator for KITS (Category 5)
@Composable
fun getKitImagePainter(imageUrl: String): Painter {
    val imageRes = when (imageUrl) {
        "img_kit_citrico" -> Res.drawable.img_kit_citrico
        "img_kit_botanico" -> Res.drawable.img_kit_botanico
        "img_kit_minimalista" -> Res.drawable.img_kit_minimalista
        "img_kit_centella" -> Res.drawable.img_kit_centella
        "img_kit_higo" -> Res.drawable.img_kit_higo
        "img_kit_rosas" -> Res.drawable.img_kit_rosas
        else -> Res.drawable.img_kit_citrico
    }
    return painterResource(imageRes)
}