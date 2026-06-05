package com.remedioz.natura.presentation.features.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import remedioznatura_kmp.composeapp.generated.resources.Res
import remedioznatura_kmp.composeapp.generated.resources.imperial_script

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onBackClick: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToEditProducts: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    val imperialFont = FontFamily(Font(Res.font.imperial_script))

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Modo Administrador",
                            fontFamily = imperialFont,
                            fontSize = 36.sp,
                            color = Color.Black
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Volver", tint = Color.Black)
                        }
                    },
                    actions = {
                        IconButton(onClick = onNavigateToNotifications) {
                            Icon(Icons.Outlined.Notifications, contentDescription = "Notificaciones", tint = Color.Black)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
                HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // --- ITEM 1: PEDIDOS ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToOrders() }
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "Pedidos",
                    fontFamily = imperialFont,
                    fontSize = 32.sp,
                    color = Color.Black
                )
            }
            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)

            // --- ITEM 2: EDITAR PRODUCTOS ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToEditProducts() }
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "Editar Productos",
                    fontFamily = imperialFont,
                    fontSize = 32.sp,
                    color = Color.Black
                )
            }
            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
        }
    }
}