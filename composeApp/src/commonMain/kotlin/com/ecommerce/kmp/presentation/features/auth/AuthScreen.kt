package com.ecommerce.kmp.presentation.features.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import e_commercekmp.composeapp.generated.resources.Res
import e_commercekmp.composeapp.generated.resources.ic_google_logo
import e_commercekmp.composeapp.generated.resources.imperial_script
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource

/**
 * ============================================================================
 * 🔐 AUTHENTICATION & ONBOARDING SCREEN
 * ============================================================================
 * * @description
 * This screen serves as the secure entry point for customer authentication.
 * It features a modern, clean UI with a custom-built OAuth provider button
 * (Google Sign-In) and handles asynchronous UI states (Loading, Errors)
 * reactively through the `AuthViewModel`.
 * * Key UX Features implemented:
 * - Scrollable container to prevent UI clipping on smaller form factors.
 * - Clear state feedback (CircularProgressIndicator during auth flows).
 * - Isolated, reusable `FloatingAuthButton` component for OAuth providers.
 * * 🔌 NOTE FOR BACKEND / SECURITY TEAM:
 * Currently, this screen triggers a simulated login flow (`loginAsClient`).
 * To implement real Google Sign-In (or Apple/Facebook), you must integrate
 * the native Auth SDKs (or a KMP wrapper like Firebase Auth / Supabase).
 * The migration steps are:
 * 1. The `onClick` should launch the native OAuth Intent/BottomSheet.
 * 2. Receive the Google `idToken` from the OS.
 * 3. Pass the token to your backend/IdP for JWT verification.
 * 4. Once validated and the session is secure, invoke the `onClientSuccess`
 * callback to route the user to their profile or checkout.
 * * @layer Presentation / Features / Auth
 * ============================================================================
 */

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onClose: () -> Unit,
    onClientSuccess: () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
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

        Spacer(modifier = Modifier.height(20.dp))

        // --- 1. CABECERA ---
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

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tu bienestar natural a un clic de distancia.",
                color = Color.DarkGray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
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

        // --- 2. ACCIÓN PRINCIPAL (CLIENTES) ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.Black)
            } else {
                FloatingAuthButton(
                    text = "Continuar con Google",
                    onClick = { viewModel.loginAsClient(onClientSuccess) }
                )
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
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

        Spacer(modifier = Modifier.weight(1f, fill = false))
        Spacer(modifier = Modifier.height(24.dp))
    }
}

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