package com.remedioz.natura.presentation.features.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import remedioznatura_kmp.composeapp.generated.resources.Res
import remedioznatura_kmp.composeapp.generated.resources.ic_google_logo
import remedioznatura_kmp.composeapp.generated.resources.imperial_script

/**
 * Pantalla de autenticación bajo el patrón UDF (Unidirectional Data Flow).
 * Delega la lógica de negocio al [AuthViewModel] y los eventos de navegación al router padre,
 * garantizando que la vista permanezca desacoplada del grafo de navegación del sistema.
 */
@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onClose: () -> Unit,
    onAdminSuccess: () -> Unit,
    onClientSuccess: () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val googleLauncher = rememberGoogleSignInLauncher(
        onAuthSuccess = { token ->
            if (token == "WEB_SUCCESS") {
                onClientSuccess()
            } else {
                viewModel.loginWithGoogleToken(token)
            }
        },
        onAuthError = { error ->
            println("Error en Auth Google: $error")
        }
    )

    if (loginSuccess) {
        onClientSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Remedioz Natura",
                fontFamily = FontFamily(Font(Res.font.imperial_script)),
                fontSize = 56.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tu bienestar natural a un clic de distancia.",
                color = Color.DarkGray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Inicia sesión para guardar tu carrito, ver tus pedidos y recibir recomendaciones personalizadas.",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // SECCIÓN DE LOGIN PARA ADMINISTRADOR
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo (Solo Admin)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black
            )
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.loginAdmin(email, password, onAdminSuccess) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(14.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Ingresar como Admin", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
            Text(" O ENTRA COMO CLIENTE ", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp))
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFF6B4BFF))
            } else {
                FloatingAuthButton(
                    text = "Continuar con Google",
                    onClick = googleLauncher
                )
            }

            Text(
                text = "Al continuar, aceptas nuestros Términos de Servicio y Política de Privacidad.",
                color = Color.LightGray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

/**
 * Componente visual Stateless (Tonto).
 * Diseñado mediante State Hoisting para ser reutilizable. Depende únicamente de sus parámetros
 * de entrada y expone la interacción mediante el callback [onClick].
 */
@Composable
fun FloatingAuthButton(
    text: String,
    logoSize: Dp = 24.dp,
    espacioDesdeIzquierda: Dp = 70.dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(BorderStroke(1.dp, Color(0xFFE0E0E0)), RoundedCornerShape(14.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = espacioDesdeIzquierda)
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_google_logo),
                contentDescription = "Logo Google",
                modifier = Modifier.size(logoSize)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}