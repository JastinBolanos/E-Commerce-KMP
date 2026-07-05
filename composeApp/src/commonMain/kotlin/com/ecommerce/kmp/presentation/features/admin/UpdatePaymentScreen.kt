package com.ecommerce.kmp.presentation.features.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import e_commercekmp.composeapp.generated.resources.Res
import e_commercekmp.composeapp.generated.resources.imperial_script
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.Font

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePaymentScreen(
    currentQrUrl: String,
    currentPhoneNumber: String,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onSaveClick: (ByteArray?, String) -> Unit
) {
    val imperialFont = FontFamily(Font(Res.font.imperial_script))
    var selectedQrBytes by remember { mutableStateOf<ByteArray?>(null) }
    var phoneNumberText by remember(currentPhoneNumber) { mutableStateOf(currentPhoneNumber) }
    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberFilePickerLauncher(
        type = PickerType.Image,
        mode = PickerMode.Single,
        title = "Selecciona tu nuevo QR de Yape o Plin"
    ) { platformFile ->
        if (platformFile != null) {
            coroutineScope.launch {
                selectedQrBytes = platformFile.readBytes()
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Metodos de Pago",
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
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Sube la imagen oficial de tu código QR. Esto se actualizará en tiempo real en la pantalla de pagos de tus clientes.",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- CAJA PARA SUBIR EL QR ---
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF9F9F9))
                    .border(
                        width = 2.dp,
                        color = if (selectedQrBytes == null) Color.LightGray else Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { launcher.launch() },
                contentAlignment = Alignment.Center
            ) {
                // LÓGICA DE PRIORIDAD DE IMAGEN
                if (selectedQrBytes != null) {
                    AsyncImage(
                        model = selectedQrBytes,
                        contentDescription = "Nuevo QR seleccionado",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (currentQrUrl.isNotEmpty()) {
                    AsyncImage(
                        model = currentQrUrl,
                        contentDescription = "QR actual",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Toca para subir de la galería", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // CAJA DE TEXTO PARA EL NÚMERO
            OutlinedTextField(
                value = phoneNumberText,
                onValueChange = { phoneNumberText = it },
                label = { Text("Número de celular (Yape/Plin)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            val hasChanges = selectedQrBytes != null || phoneNumberText != currentPhoneNumber

            // --- BOTÓN GUARDAR ---
            Button(
                onClick = {
                    if (hasChanges && !isLoading) {
                        onSaveClick(selectedQrBytes, phoneNumberText)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = hasChanges && !isLoading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black, disabledContainerColor = Color.LightGray)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}