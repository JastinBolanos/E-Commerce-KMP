package com.ecommerce.kmp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ============================================================================
 * 🏷️ CATEGORY FILTER CHIP ROW (STATELESS)
 * ============================================================================
 * * @description
 * This component renders a horizontally scrollable row of category filter chips.
 * It is strictly stateless, adhering perfectly to the Unidirectional Data Flow
 * (UDF) pattern. It receives the `selectedCategory` as a read-only state and
 * emits user intent upwards via the `onCategorySelected` lambda.
 * * Key Compose Performance Best Practices implemented:
 * - List Virtualization: Utilizes `LazyRow` to efficiently recycle chip
 * composables off-screen, maintaining performance even if the category list grows.
 * - Modifier Delegation: Accepts a base `Modifier` to allow parent containers
 * to dictate external spacing without breaking internal padding.
 * * 🔌 NOTE FOR BACKEND TEAM:
 * The `categories` list is currently hardcoded for showcase purposes. When
 * migrating to production, this component should receive the `List<String>`
 * as a parameter, populated dynamically by a `GET /api/v1/categories` endpoint.
 * * @layer Presentation / Components
 * ============================================================================
 */

@Composable
fun CategoryFilter(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("Todos", "Kits", "Tratamientos", "Belleza", "Cuidados", "Piel", "Otros")

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable {
                        onCategorySelected(category)
                    }
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Color.Black else Color.LightGray,
                        shape = RoundedCornerShape(50)
                    )
                    .background(
                        color = if (isSelected) Color.Black else Color.White,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category,
                    color = if (isSelected) Color.White else Color.DarkGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}