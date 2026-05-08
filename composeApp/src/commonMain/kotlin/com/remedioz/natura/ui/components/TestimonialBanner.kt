package com.remedioz.natura.ui.components

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
            .background(Color(0xFF8D7C70)) // Color temporal marrón claro
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(0.65f) // Ocupa el 65% del ancho para dejar espacio a la foto
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
                text = "Más que una tienda, somos un equipo dedicado a guiarte en tu camino hacia una vida más saludable con soluciones naturales diseñadas para tu bienestar diario.",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text("Johana Quispe Ortiz", color = Color.White, fontWeight = FontWeight.Bold)
            Text("Especialista en Salud y Belleza", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}