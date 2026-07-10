package com.ecommerce.kmp.presentation.features.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import io.github.vinceglb.filekit.core.PickerMode
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontFamily
import org.jetbrains.compose.resources.Font
import com.ecommerce.kmp.domain.model.Product
import com.ecommerce.kmp.presentation.components.CategoryFilter
import com.ecommerce.kmp.presentation.components.LandscapeProductCard
import com.ecommerce.kmp.presentation.components.ProductCard
import com.ecommerce.kmp.presentation.components.SearchInput
import com.ecommerce.kmp.presentation.components.getKitImagePainter
import com.ecommerce.kmp.presentation.components.getProductImagePainter
import e_commercekmp.composeapp.generated.resources.Res
import e_commercekmp.composeapp.generated.resources.imperial_script
import androidx.compose.ui.platform.LocalFocusManager

/**
 * ============================================================================
 * ✏️ ADMIN PRODUCT & KIT MANAGEMENT SCREEN
 * ============================================================================
 * * @description
 * This screen provides a highly interactive CRUD (Create, Read, Update, Delete)
 * interface for the Store Administrator. It features a reactive search and
 * filter system, alongside a unified `ModalBottomSheet` that intelligently
 * handles both new product creation and existing product mutation.
 * * Key UX Features implemented:
 * - Smart Spacing: Conditionally collapses layout bounds when categories are empty.
 * - Multiplatform FilePicker: Uses `FileKit` to read native image files into RAM.
 * - Reactive Form UI: Real-time form rendering based on the `selectedProduct` state.
 * * 🔌 NOTE FOR BACKEND / CONTENT TEAM:
 * The form currently packages the input fields into a new `Product` data class
 * and pairs it with the raw `selectedImageBytes`.
 * When migrating to a Production API:
 * 1. Implement secure presigned URLs for image uploads (e.g., S3 `putObject`).
 * 2. Ensure the Dropdown Menu (`allowedCategories`) is hydrated dynamically
 * via a `/api/v1/categories` endpoint rather than being hardcoded.
 * 3. Add Server-Side Validation handling (e.g., catching 400 Bad Request if
 * the price format is invalid or the name is duplicated) and map it back
 * to the UI text fields.
 * * @layer Presentation / Features / Admin
 * ============================================================================
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductsScreen(
    onBackClick: () -> Unit,
    viewModel: AdminViewModel
) {
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var isSearchFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val kitsProducts = filteredProducts.filter { it.category.equals("Kits", ignoreCase = true) }
    val verticalProducts = filteredProducts.filter { !it.category.equals("Kits", ignoreCase = true) }

    Scaffold(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) { focusManager.clearFocus() },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Editar Productos",
                        fontFamily = FontFamily(Font(Res.font.imperial_script)),
                        fontSize = 36.sp,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
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
            SearchInput(
                query = searchQuery,
                onQueryChange = { viewModel.updateSearchQuery(it) },
                isFocused = isSearchFocused,
                onFocusChange = { isSearchFocused = it },
                modifier = Modifier.padding(top = 8.dp)
            )

            CategoryFilter(
                selectedCategory = selectedCategory,
                onCategorySelected = { viewModel.updateCategory(it) },
                modifier = Modifier.padding(vertical = 16.dp)
            )

            if (verticalProducts.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
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
                Spacer(modifier = Modifier.height(20.dp))
            }

            if (kitsProducts.isNotEmpty()) {
                if (selectedCategory.equals("Todos", ignoreCase = true)) {
                    Text(
                        "Kits y Promociones",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
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
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductSheetContent(
    product: Product?,
    viewModel: AdminViewModel,
    onCloseSheet: () -> Unit
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var priceInput by remember { mutableStateOf(if (product != null && product.price > 0.0) product.price.toString() else "") }
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
                val isKit = category.equals("Kits", ignoreCase = true)
                Image(
                    painter = if (isKit) getKitImagePainter(product!!.imageUrl) else getProductImagePainter(product!!.imageUrl),
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
                value = priceInput,
                onValueChange = { priceInput = it },
                label = { Text("Precio") },
                prefix = { Text("S/ ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            )

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

        Button(
            onClick = {
                val productToSave = Product(
                    id = product?.id ?: "",
                    name = name,
                    price = priceInput.toDoubleOrNull() ?: 0.0,
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