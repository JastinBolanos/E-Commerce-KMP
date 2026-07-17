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

/**
 * ============================================================================
 * 🛍️ PRODUCT DETAIL MODAL DIALOG
 * ============================================================================
 * * @description
 * This stateless component renders a responsive modal dialog to display
 * full product details, including a large cover image, price, and description.
 * * Key Compose UI/UX Features implemented:
 * - Custom Dimensions: Overrides default platform dialog constraints using
 * `DialogProperties(usePlatformDefaultWidth = false)` to achieve a 90% width
 * and 85% height immersive view.
 * - Z-Index Layering: Employs a floating Action Button (Close) anchored
 * to the TopEnd with a semi-transparent background and elevated `zIndex`
 * to ensure it remains clickable regardless of image dimensions.
 * - Scrollable Content: Implements `verticalScroll` to guarantee accessibility
 * on smaller devices or when product descriptions are lengthy.
 * * @layer Presentation / Components
 * ============================================================================
 */

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
                // --- Floating Close Button (X) ---
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(50))
                        .zIndex(1f)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // --- 1. LARGE IMAGE ---
                    Image(
                        painter = imagePainter,
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp)
                            .background(Color(0xFFEBEBEB))
                    )

                    // --- 2. DETAILS AND DESCRIPTION ---
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
                            text = "\$${product.price.format(2)}",
                            fontSize = 20.sp,
                            color = Color(0xFF67B2C5),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "About this product",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = product.description.ifEmpty { "Description not available for this product." },
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