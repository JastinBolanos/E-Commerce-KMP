package com.ecommerce.kmp.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import e_commercekmp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun TestimonialBanner(modifier: Modifier = Modifier) {

    // --- MOTOR DE ANIMACIÓN INFINITA ---
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Contenedor principal
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(400.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {

        // --- CAPA 1: EL EFECTO DE LUZ GIRATORIA (Fondo) ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    rotate(angle) {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    Color(0xFF70B6C7),
                                    Color.White,
                                    Color(0xFF76B8C9),
                                    Color.Transparent,
                                    Color.Transparent
                                )
                            ),
                            radius = size.width
                        )
                    }
                }
        )

        // --- CAPA 2: IMAGEN ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Image(
                painter = painterResource(Res.drawable.img_banner_full),
                contentDescription = "Banner Testimonial Especialista",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}