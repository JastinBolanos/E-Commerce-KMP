package com.remedioz.natura.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExploreProductsSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = "Explorar Productos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Primera fila deslizable
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item { CategoryChip("NHT Products", Color(0xFFE57373)) } // Rojo
            item { CategoryChip("Cafee", Color(0xFFAED581)) }        // Verde
            item { CategoryChip("Tratamientos", Color(0xFF303F9F)) } // Azul Oscuro
            item { CategoryChip("Belleza", Color(0xFFF06292)) }      // Fucsia
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Segunda fila deslizable
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item { CategoryChip("Cuidados", Color(0xFF64B5F6)) }     // Celeste
            item { CategoryChip("Piel", Color(0xFFBA68C8)) }         // Morado
            item { CategoryChip("Otros", Color(0xFFFFD54F)) }        // Amarillo
        }
    }
}

@Composable
fun CategoryChip(text: String, dotColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(1.dp, Color.LightGray, RoundedCornerShape(50))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(dotColor, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
}