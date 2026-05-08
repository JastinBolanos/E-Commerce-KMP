package com.remedioz.natura.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.remedioz.natura.domain.model.Product
import com.remedioz.natura.ui.components.* // Importamos tus componentes ya creados

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(onBackClick: () -> Unit) {
    // --- ESTADOS DE CONTROL ---
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    // Datos de prueba (Luego vendrán de Firebase)
    val verticalProducts = listOf(
        Product(name = "Crema Facial", price = "$25", category = "Tratamientos"),
        Product(name = "Aceite de Coco", price = "$15", category = "Piel")
    )
    val kitsProducts = listOf(
        Product(name = "Kit Relajante Total", price = "$45", category = "Kits"),
        Product(name = "Rutina Cuidado Facial", price = "$60", category = "Kits")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MODO ADMINISTRADOR", fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        // Contenedor principal con Scroll para que quepa todo como en la tienda
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            // 1. Buscador e Iconos (Estética idéntica)
            SearchInput(modifier = Modifier.padding(top = 8.dp))
            CategoryFilter(modifier = Modifier.padding(vertical = 16.dp))

            // 2. Primera Fila: Productos Verticales
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(verticalProducts) { product ->
                    ProductCard(
                        name = product.name,
                        price = product.price,
                        initialIsInCart = false, // <--- ¡AQUÍ ESTÁ EL CAMBIO!
                        modifier = Modifier
                            .width(230.dp)
                            .clickable {
                                selectedProduct = product
                                showSheet = true
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Segunda Fila: Kits (Rectángulos echados)
            Text(
                "Kits y Promociones",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(kitsProducts) { product ->
                    LandscapeProductCard(
                        name = product.name,
                        price = product.price,
                        modifier = Modifier.clickable {
                            selectedProduct = product
                            showSheet = true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp)) // Espacio para no chocar con el final
        }

        // --- BANDEJA FLOTANTE (MODAL BOTTOM SHEET) ---
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState,
                containerColor = Color.White,
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.LightGray) }
            ) {
                // El contenido de la bandeja de edición
                EditProductSheetContent(
                    product = selectedProduct,
                    onSave = { updatedProduct ->
                        println("Guardando cambios de: ${updatedProduct.name}")
                        showSheet = false
                    },
                    onDelete = {
                        println("Borrando producto")
                        showSheet = false
                    }
                )
            }
        }
    }
}

@Composable
fun EditProductSheetContent(
    product: Product?,
    onSave: (Product) -> Unit,
    onDelete: () -> Unit
) {
    // Variables temporales para editar
    var name by remember { mutableStateOf(product?.name ?: "") }
    var price by remember { mutableStateOf(product?.price ?: "") }
    var category by remember { mutableStateOf(product?.category ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "Descripción del producto natural...") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Editar Contenido", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        // Botón para cambiar foto (Simulado profesional)
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFF0F0F0), RoundedCornerShape(16.dp))
                .clickable { /* Abrir galería */ },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = Color.Gray)
                Text("Cambiar Foto", fontSize = 10.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Campos de Texto profesionales
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth().height(100.dp),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botones de Acción
        Button(
            onClick = { onSave(product!!.copy(name = name, price = price)) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold)
        }

        TextButton(onClick = onDelete, modifier = Modifier.padding(top = 8.dp)) {
            Text("Eliminar Producto", color = Color.Red)
        }
    }
}