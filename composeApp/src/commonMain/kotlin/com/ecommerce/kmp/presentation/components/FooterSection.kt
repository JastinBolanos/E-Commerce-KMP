package com.ecommerce.kmp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FooterSection(modifier: Modifier = Modifier, onAdminLoginClick: () -> Unit) {
    // --- MEMORIA DEL COMPONENTE ---
    var isAdminExpanded by remember { mutableStateOf(false) }
    var emailAdmin by remember { mutableStateOf("") }
    var passwordAdmin by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0A0A0A))
            .padding(32.dp)
    ) {
        // Título de la marca
        Text("Remedioz Natura", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        // Descripción real en español
        Text(
            text = "Plataforma de e-commerce dedicada a la venta de productos naturales para el bienestar y la salud integral.",
            color = Color.Gray,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Menú inferior
        Text("MENU", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(16.dp))

        val menuItems = listOf("Ayuda", "Contactanos", "Sobre Nosotros", "Preguntas Frecuentes", "Terminos Y Condiciones")
        menuItems.forEach { item ->
            Text(
                text = item,
                color = Color.LightGray,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- PANEL DE ADMINISTRADOR OCULTO ---
        if (isAdminExpanded) {
            Spacer(modifier = Modifier.height(16.dp))

            // Input de Email
            OutlinedTextField(
                value = emailAdmin,
                onValueChange = { emailAdmin = it },
                placeholder = { Text("Ingresa Email de Administrador", color = Color.Gray) },
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.DarkGray,
                    cursorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Input de Contraseña
            OutlinedTextField(
                value = passwordAdmin,
                onValueChange = { passwordAdmin = it },
                placeholder = { Text("Contraseña", color = Color.Gray) },
                shape = RoundedCornerShape(50),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(0.7f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.DarkGray,
                    cursorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Ingresar
            Button(
                onClick = { onAdminLoginClick() },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Ingresar", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        // --- LA FLECHITA TÁCTIL ---
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = if (isAdminExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Expandir Admin",
                tint = Color.White,
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        isAdminExpanded = !isAdminExpanded
                    }
            )
        }
    }
}