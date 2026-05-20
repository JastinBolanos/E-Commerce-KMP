package com.remedioz.natura.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.remedioz.natura.ui.components.CategoryFilter
import com.remedioz.natura.ui.components.FooterSection
import com.remedioz.natura.ui.components.LandscapeProductCard
import com.remedioz.natura.ui.components.ProductCard
import com.remedioz.natura.ui.components.SearchInput
import com.remedioz.natura.ui.components.TestimonialBanner
import com.remedioz.natura.ui.components.TopNavBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.remedioz.natura.ui.screens.CartScreen
import androidx.compose.runtime.collectAsState
import com.remedioz.natura.ui.viewmodel.HomeViewModel
import androidx.compose.foundation.lazy.items

/**
 * PANTALLA PRINCIPAL DE LA TIENDA (HomeScreen)
 * @param onAdminClick Callback para navegar a la vista de Administrador.
 * @param onAuthClick Callback para delegar la navegación de Autenticación hacia App.kt.
 * @param viewModel El ViewModel que provee el catálogo en tiempo real desde Firebase.
 */
@Composable
fun HomeScreen(
    onAdminClick: () -> Unit,
    onAuthClick: () -> Unit,
    viewModel: HomeViewModel
) {
    // Estado para controlar qué vemos dentro de la sección "Home" (Solo Tienda o Carrito)
    var currentScreen by remember { mutableStateOf("STORE") }

    // --- ESCUCHAMOS LOS DATOS REALES DE FIREBASE ---
    // El 'collectAsState' hace que la pantalla se redibuje automáticamente si hay cambios en la nube
    val allProducts by viewModel.products.collectAsState()

    // Filtramos las listas en tiempo real según la categoría
    val kitsProducts = allProducts.filter { it.category.equals("Kits", ignoreCase = true) }
    val verticalProducts = allProducts.filter { !it.category.equals("Kits", ignoreCase = true) }

    // --- LÓGICA DE NAVEGACIÓN INTERNA ---
    if (currentScreen == "STORE") {

        // Si estamos en la tienda, dibujamos todo el catálogo principal
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentPadding = PaddingValues(bottom = 0.dp)
        ) {
            // 1. CABECERA: Buscador, Categorías y Navegación
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TopNavBar(
                        onCartClick = { currentScreen = "CART" },
                        onProfileClick = { onAuthClick() }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    SearchInput()
                    Spacer(modifier = Modifier.height(16.dp))

                    CategoryFilter()
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // 2. PRODUCTOS: El carrusel horizontal (Tarjetas Verticales)
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Generamos dinámicamente las tarjetas consumiendo los datos de Firebase
                    items(verticalProducts) { product ->
                        ProductCard(
                            product = product,
                            modifier = Modifier.width(180.dp)
                        )
                    }
                }
            }

            // 3. TERCERA SECCIÓN (Tarjetas Apaisadas - Kits y Promociones)
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Kits y Promociones",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(kitsProducts) { product ->
                            LandscapeProductCard(
                                product = product,
                                modifier = Modifier
                            )
                        }
                    }
                }
            }

            // 4. FINAL DE LA PÁGINA: Testimonios y Footer
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(40.dp))
                    TestimonialBanner()
                    Spacer(modifier = Modifier.height(40.dp))
                    FooterSection(onAdminLoginClick = { onAdminClick() })
                }
            }
        }

    } else if (currentScreen == "CART") {
        // Mostramos el carrito de compras y permitimos volver al Store
        CartScreen(
            onBackClick = { currentScreen = "STORE" }
        )
    }
}