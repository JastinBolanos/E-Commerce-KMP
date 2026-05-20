package com.remedioz.natura.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.remedioz.natura.ui.components.*
import com.remedioz.natura.ui.viewmodel.AdminViewModel
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import io.github.vinceglb.filekit.core.PickerMode
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onBackClick: () -> Unit,
    viewModel: AdminViewModel
) {
    // --- ESTADOS DE CONTROL ---
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    // --- LOS DATOS REALES DE FIREBASE ---
    val allProducts by viewModel.products.collectAsState()

    // Filtramos las listas según lo que llegue de la nube
    val kitsProducts = allProducts.filter { it.category.equals("Kits", ignoreCase = true) }
    val verticalProducts = allProducts.filter { !it.category.equals("Kits", ignoreCase = true) }

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
        },
        // --- NUEVO: BOTÓN PARA AGREGAR PRODUCTOS ---
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedProduct = null
                    showSheet = true
                },
                containerColor = Color.Black,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Producto")
            }
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
            // 1. Buscador e Iconos
            SearchInput(modifier = Modifier.padding(top = 8.dp))
            CategoryFilter(modifier = Modifier.padding(vertical = 16.dp))

            // 2. Primera Fila: Productos Verticales
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(verticalProducts) { product ->
                    ProductCard(
                        product = product,
                        showCartIcon = false,
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
                        product = product,
                        showCartIcon = false,
                        modifier = Modifier.clickable {
                            selectedProduct = product
                            showSheet = true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        // --- BANDEJA FLOTANTE ---
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
                    viewModel = viewModel,
                    onCloseSheet = { showSheet = false }
                )
            }
        }
    }
}

@Composable
fun EditProductSheetContent(
    product: Product?,
    viewModel: AdminViewModel,
    onCloseSheet: () -> Unit
) {
    // --- VARIABLES DE ESTADO ---
    var name by remember { mutableStateOf(product?.name ?: "") }
    var price by remember { mutableStateOf(product?.price ?: "") }
    var category by remember { mutableStateOf(product?.category ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }

    // Estado para la imagen
    var selectedImageBytes by remember { mutableStateOf<ByteArray?>(null) }

    // Corrutina segura para la UI
    val coroutineScope = rememberCoroutineScope()

    // Selector de imágenes (FileKit)
    val launcher = rememberFilePickerLauncher(
        type = PickerType.Image,
        mode = PickerMode.Single,
        title = "Selecciona una foto para el producto"
    ) { platformFile ->
        if (platformFile != null) {
            coroutineScope.launch {
                selectedImageBytes = platformFile.readBytes()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(if (product?.id.isNullOrEmpty()) "Nuevo Producto" else "Editar Contenido", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        // Botón para cambiar foto (AHORA MUESTRA LA IMAGEN)
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFF0F0F0), RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp)) // Para que la foto no se salga de las esquinas
                .clickable { launcher.launch() },
            contentAlignment = Alignment.Center
        ) {
            // Si el usuario acaba de elegir una foto nueva (Bytes)
            if (selectedImageBytes != null) {
                AsyncImage(
                    model = selectedImageBytes, // Coil entiende los bytes directamente
                    contentDescription = "Foto nueva",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            // Si no hay foto nueva, pero el producto ya tiene una URL en Firebase
            else if (!product?.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = product!!.imageUrl,
                    contentDescription = "Foto actual",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            // Si no hay nada (Producto nuevo y sin seleccionar foto)
            else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = Color.Gray)
                    Text("Cambiar Foto", fontSize = 10.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio") },

                prefix = { Text("S/ ") },

                // Abre el teclado de números automáticamente ---
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Categoría") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth().height(100.dp), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(32.dp))

        // Botón Guardar
        Button(
            onClick = {
                val productToSave = Product(
                    id = product?.id ?: "",
                    name = name,
                    price = price,
                    category = category,
                    description = description,
                    imageUrl = product?.imageUrl ?: ""
                )
                viewModel.saveProduct(productToSave, selectedImageBytes)
                onCloseSheet()
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold)
        }

        if (!product?.id.isNullOrEmpty()) {
            TextButton(
                onClick = {
                    viewModel.deleteProduct(product!!.id)
                    onCloseSheet()
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Eliminar Producto", color = Color.Red)
            }
        }
    }
}