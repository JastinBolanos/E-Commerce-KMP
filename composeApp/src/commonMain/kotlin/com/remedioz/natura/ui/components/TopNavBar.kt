package com.remedioz.natura.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton // <-- IMPORTANTE: Herramienta para hacer el icono clickeable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import remedioznatura.composeapp.generated.resources.Res
import remedioznatura.composeapp.generated.resources.imperial_script

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
        // --- INICIO DEL LOGO REMEDIOZ NATURA ---
        Text(
            text = "Remedioz Natura",
            fontFamily = FontFamily(Font(Res.font.imperial_script)),
            fontWeight = FontWeight.Normal,
            fontSize = 38.sp,
            lineHeight = 14.8.sp,
            letterSpacing = 0.sp,
            color = Color.Black
        )
        // --- FIN DEL LOGO ---

        // Reduje el spacedBy a 4.dp porque los IconButton ya traen su propio espacio invisible para el dedo
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {

            // --- BOTÓN DEL CARRITO CONECTADO ---
            IconButton(onClick = { onCartClick() }) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = Color.Black)
            }

            // --- BOTÓN DE PERFIL ---
            IconButton(onClick = { onProfileClick() }) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Perfil", tint = Color.Black)
            }
        }
    }
}