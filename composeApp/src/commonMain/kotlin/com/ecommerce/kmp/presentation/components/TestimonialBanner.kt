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

/**
 * ============================================================================
 * 💫 ANIMATED TESTIMONIAL BANNER (GPU OPTIMIZED)
 * ============================================================================
 * * @description
 * This component renders a promotional banner featuring a continuous,
 * rotating glowing border effect. It is strictly presentational and stateless.
 * * Key Compose Performance Best Practices implemented:
 * - Deferred State Reading: The infinite rotation animation (`angle`) is read
 * exclusively inside the `drawBehind` lambda. This bypasses the Compose
 * Recomposition and Layout phases entirely, sending the rotation math directly
 * to the GPU drawing phase. This guarantees a silky smooth 60 FPS animation
 * without draining the device's battery or dropping frames.
 * - Layer Masking: Creates a dynamic border by nesting a solid image box
 * with a `3.dp` padding over the rotating `sweepGradient` background.
 * * @layer Presentation / Components
 * ============================================================================
 */

@Composable
fun TestimonialBanner(modifier: Modifier = Modifier) {

    // --- INFINITE ANIMATION ENGINE ---
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Main container
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(400.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {

        // --- LAYER 1: ROTATING LIGHT EFFECT (Background) ---
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

        // --- LAYER 2: IMAGE ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Image(
                painter = painterResource(Res.drawable.img_banner_full),
                contentDescription = "Specialist Testimonial Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}