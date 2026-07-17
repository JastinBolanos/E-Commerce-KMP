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

/**
 * ============================================================================
 * 🕵️‍♂️ FOOTER SECTION & HIDDEN ADMIN PORTAL
 * ============================================================================
 * * @description
 * This component acts as the standard application footer, displaying brand
 * information, standard legal/help routing links, and copyright data.
 * Crucially, it houses a hidden "Easter Egg" administrative login portal
 * that expands via an interactive chevron, keeping the consumer-facing UX
 * uncluttered while maintaining admin accessibility.
 * * Key Compose UI/UX Features implemented:
 * - Expandable State: Uses local `remember { mutableStateOf(false) }` to
 * conditionally animate and render the admin input fields.
 * - Secure Text Entry: Utilizes `PasswordVisualTransformation` to mask sensitive
 * administrative credentials from screen-readers and shoulder-surfers.
 * - Dark Mode Aesthetics: Hardcoded dark theme (`Color(0xFF0A0A0A)`) to visually
 * anchor the bottom of the scrollable lists.
 * * 🔌 NOTE FOR BACKEND / AUTH TEAM:
 * The current `onAdminLoginClick` lambda is parameter-less, acting purely as a
 * routing trigger for showcase purposes. When migrating to real authentication,
 * update the lambda signature to `(email: String, password: String) -> Unit`
 * to pass the captured internal states directly to the `AuthViewModel` for
 * JWT validation.
 * * @layer Presentation / Components
 * ============================================================================
 */

@Composable
fun FooterSection(modifier: Modifier = Modifier, onAdminLoginClick: () -> Unit) {
    // --- COMPONENT MEMORY ---
    var isAdminExpanded by remember { mutableStateOf(false) }
    var emailAdmin by remember { mutableStateOf("") }
    var passwordAdmin by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0A0A0A))
            .padding(32.dp)
    ) {
        // Brand title
        Text("Remedioz Natura", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        // Real description
        Text(
            text = "E-commerce platform dedicated to the sale of natural products for comprehensive health and well-being.",
            color = Color.Gray,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Bottom menu
        Text("MENU", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(16.dp))

        val menuItems = listOf("Help", "Contact Us", "About Us", "FAQ", "Terms and Conditions")
        menuItems.forEach { item ->
            Text(
                text = item,
                color = Color.LightGray,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- HIDDEN ADMIN PANEL ---
        if (isAdminExpanded) {
            Spacer(modifier = Modifier.height(16.dp))

            // Email Input
            OutlinedTextField(
                value = emailAdmin,
                onValueChange = { emailAdmin = it },
                placeholder = { Text("Enter Admin Email", color = Color.Gray) },
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

            // Password Input
            OutlinedTextField(
                value = passwordAdmin,
                onValueChange = { passwordAdmin = it },
                placeholder = { Text("Password", color = Color.Gray) },
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

            // Login Button
            Button(
                onClick = { onAdminLoginClick() },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Login", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        // --- TOUCHABLE ARROW ---
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = if (isAdminExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand Admin",
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