package com.remedioz.natura.presentation.features.admin

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
import androidx.compose.material3.ExperimentalMaterial3Api
import com.remedioz.natura.presentation.components.CategoryFilter
import com.remedioz.natura.presentation.components.LandscapeProductCard
import com.remedioz.natura.presentation.components.ProductCard
import com.remedioz.natura.presentation.components.SearchInput

/**
 * Consola de Administración (UI Host protegido).
 * Implementa un patrón maestro-detalle usando [ModalBottomSheet] para la edición in-place.
 * Escucha el estado reactivo del [AdminViewModel] para reflejar filtros y búsquedas
 * en tiempo real sin mutar la fuente de datos original.
 *
 * @param onBackClick Callback para salir de la consola y regresar a la vista pública.
 * @param viewModel Proveedor del estado del inventario y orquestador de las mutaciones CRUD.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onBackClick: () -> Unit,
    viewModel: AdminViewModel
) {
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val kitsProducts = filteredProducts.filter { it.category.equals("Kits", ignoreCase = true) }
    val verticalProducts = filteredProducts.filter { !it.category.equals("Kits", ignoreCase = true) }

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
        // --- BOTÓN PARA AGREGAR PRODUCTOS ---
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            // 1. Buscador y Filtros Reactivos
            SearchInput(
                query = searchQuery,
                onQueryChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier.padding(top = 8.dp)
            )
            CategoryFilter(
                selectedCategory = selectedCategory,
                onCategorySelected = { viewModel.updateCategory(it) },
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // 2. Primera Fila: Productos Verticales
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(verticalProducts) { product ->
                    ProductCard(
                        product = product,
                        showCartIcon = false,
                        isAdminView = true,
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
                        isAdminView = true,
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
                EditProductSheetContent(
                    product = selectedProduct,
                    viewModel = viewModel,
                    onCloseSheet = { showSheet = false }
                )
            }
        }
    }
}

/**
 * Formulario de creación/edición de productos (Stateful Component).
 * Gestiona su propio estado temporal (Draft State) antes de confirmar la mutación
 * hacia el ViewModel. Utiliza un menú desplegable estricto para blindar la entrada
 * de datos en el campo de categorías y previene errores de tipografía en la base de datos.
 *
 * @param product Entidad a editar. Si es null, el formulario actúa en modo "Creación".
 * @param viewModel Instancia requerida para delegar la persistencia final (Upload/Save).
 * @param onCloseSheet Callback para colapsar la bandeja tras guardar o eliminar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductSheetContent(
    product: Product?,
    viewModel: AdminViewModel,
    onCloseSheet: () -> Unit
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var price by remember { mutableStateOf(product?.price ?: "") }
    var category by remember { mutableStateOf(product?.category?.takeIf { it.isNotEmpty() } ?: "Tratamientos") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    val allowedCategories = listOf("Kits", "Tratamientos", "Belleza", "Cuidados", "Piel", "Otros")
    var expandedDropdown by remember { mutableStateOf(false) }
    var selectedImageBytes by remember { mutableStateOf<ByteArray?>(null) }
    val coroutineScope = rememberCoroutineScope()

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

        // Botón cambiar foto
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFF0F0F0), RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .clickable { launcher.launch() },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageBytes != null) {
                AsyncImage(
                    model = selectedImageBytes,
                    contentDescription = "Foto nueva",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else if (!product?.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = product!!.imageUrl,
                    contentDescription = "Foto actual",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            )

            // --- MENU PARA CATEGORÍAS ---
            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { expandedDropdown = !expandedDropdown },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false }
                ) {
                    allowedCategories.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                category = selectionOption
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth().height(100.dp), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(32.dp))

        //botón para Guardar
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