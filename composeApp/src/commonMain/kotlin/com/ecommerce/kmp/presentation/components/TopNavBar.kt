package com.ecommerce.kmp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import e_commercekmp.composeapp.generated.resources.Res
import e_commercekmp.composeapp.generated.resources.imperial_script
import org.jetbrains.compose.resources.Font

/**
 * ============================================================================
 * 🔝 TOP NAVIGATION BAR (STATELESS COMPONENT)
 * ============================================================================
 * * @description
 * This is a purely presentational, stateless Composable component representing
 * the top application bar. It strictly adheres to the Unidirectional Data Flow
 * (UDF) pattern by elevating all user interactions (clicks) up to the parent
 * orchestrator via lambda functions (`onCartClick`, `onProfileClick`).
 * * Key Compose Best Practices implemented:
 * - Modifier Delegation: Accepts a default `Modifier` as the first parameter,
 * allowing parent containers to adjust layout bounds flexibly without altering
 * the internal component logic.
 * - Multiplatform Typography: Securely loads custom fonts (`imperial_script`)
 * using the JetBrains Compose Resources library.
 * * @layer Presentation / Components
 * ============================================================================
 */

@Composable
fun TopNavBar(
    modifier: Modifier = Modifier,
    onCartClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- REMEDIOZ NATURA LOGO START ---
        Text(
            text = "Remedioz Natura",
            fontFamily = FontFamily(Font(Res.font.imperial_script)),
            fontWeight = FontWeight.Normal,
            fontSize = 38.sp,
            lineHeight = 14.8.sp,
            letterSpacing = 0.sp,
            color = Color.Black
        )
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {

            // --- CART BUTTON ---
            IconButton(onClick = { onCartClick() }) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color.Black)
            }

            // --- PROFILE BUTTON ---
            IconButton(onClick = { onProfileClick() }) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Profile", tint = Color.Black)
            }
        }
    }
}