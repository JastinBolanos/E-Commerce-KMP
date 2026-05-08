package com.remedioz.natura.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryFilter(modifier: Modifier = Modifier) {
    val categories = listOf("Tratamientos", "Belleza", "Cuidados", "Piel", "Otros")

    // 1. Aquí creamos la "memoria" del componente.
    // Le decimos que por defecto arranque seleccionando la primera opción ("Tratamientos")
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            // 2. Evaluamos si esta categoría específica es la que está guardada en la memoria
            val isSelected = category == selectedCategory

            Box(
                modifier = Modifier
                    // 3. Clip recorta el efecto de "onda" al hacer clic para que no se salga de los bordes curvos
                    .clip(RoundedCornerShape(50))
                    // 4. Clickable hace que el botón reaccione al dedo y actualice la memoria
                    .clickable {
                        selectedCategory = category
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