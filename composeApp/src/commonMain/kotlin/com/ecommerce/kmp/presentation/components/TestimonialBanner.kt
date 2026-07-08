package com.ecommerce.kmp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TestimonialBanner(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF8D7C70))
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(0.65f)
        ) {
            Text("2026", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tu salud, nuestra prioridad.",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Descubre el poder de la naturaleza en cada aspecto de tu vida. Desde el cuidado profundo de tu piel hasta aromas que inspiran, te ofrecemos una selección integral para resaltar tu belleza y bienestar auténtico.",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text("Luciana Valdivia Montes", color = Color.White, fontWeight = FontWeight.Bold)
            Text("Especialista en Salud y Belleza", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}