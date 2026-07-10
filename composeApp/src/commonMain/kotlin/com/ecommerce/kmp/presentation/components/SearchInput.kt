package com.ecommerce.kmp.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

/**
 * ============================================================================
 * 🔎 ANIMATED SEARCH INPUT (STATELESS & GPU-OPTIMIZED)
 * ============================================================================
 * * @description
 * This component provides a custom-styled search field featuring an interactive,
 * GPU-accelerated glowing border that activates exclusively upon user focus.
 * It strictly follows the Unidirectional Data Flow (UDF) architecture by
 * hoisting the `query` and `isFocused` states to the parent orchestrator.
 * * Key Compose Performance Best Practices implemented:
 * - Conditional GPU Drawing: The infinite rotation animation (`angle`) is executed
 * inside a `drawBehind` lambda, bypassing the Compose layout phase. Crucially,
 * the animation only renders when `isFocused == true`, falling back to a cheap
 * static `drawRect` otherwise to preserve device battery during idle states.
 * - Clever Layer Masking: Achieves a dynamic `1.2.dp` border by nesting
 * `clip` and `padding` modifiers over the drawn background.
 * - Focus Delegation: Seamlessly interacts with `LocalFocusManager` to dismiss
 * the software keyboard when the trailing search icon is pressed.
 * * @layer Presentation / Components
 * ============================================================================
 */

@Composable
fun SearchInput(
    query: String,
    onQueryChange: (String) -> Unit,
    isFocused: Boolean,
    onFocusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val infiniteTransition = rememberInfiniteTransition(label = "border_animation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(50))
            .drawBehind {
                if (isFocused) {
                    rotate(angle) {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                colors = listOf(
                                    Color(0xFFE0E0E0),
                                    Color(0xFFE0E0E0),
                                    Color(0xFF9E9E9E),
                                    Color(0xFF505050),
                                    Color(0xFF9E9E9E),
                                    Color(0xFFE0E0E0),
                                    Color(0xFFE0E0E0)
                                )
                            ),
                            radius = size.width
                        )
                    }
                } else {
                    drawRect(Color(0xFFD2D2D2))
                }
            }
            .padding(1.2.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.White)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Buscar Productos", color = Color.Gray) },
            trailingIcon = {
                IconButton(onClick = { focusManager.clearFocus() }) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.Gray)
                }
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxSize()
                .onFocusChanged { onFocusChange(it.isFocused) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                cursorColor = Color.Black
            )
        )
    }
}