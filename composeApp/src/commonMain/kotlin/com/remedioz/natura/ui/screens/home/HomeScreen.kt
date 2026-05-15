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
import com.remedioz.natura.ui.screens.AuthScreen
import com.remedioz.natura.ui.screens.CartScreen
import androidx.compose.runtime.collectAsState
import com.remedioz.natura.ui.viewmodel.HomeViewModel
import androidx.compose.foundation.lazy.items


@Composable
fun HomeScreen(
    onAdminClick: () -> Unit,
    viewModel: HomeViewModel // <-- NUEVO: Recibimos el ViewModel aquí
) {
    // Estado para controlar qué vemos dentro de la sección "Home"
    var currentScreen by remember { mutableStateOf("STORE") }

    // --- NUEVO: ESCUCHAMOS LOS DATOS REALES DE FIREBASE ---
    val allProducts by viewModel.products.collectAsState()

    // Filtramos las listas en tiempo real según la categoría
    val kitsProducts = allProducts.filter { it.category.equals("Kits", ignoreCase = true) }
    val verticalProducts = allProducts.filter { !it.category.equals("Kits", ignoreCase = true) }

    // --- LÓGICA DE NAVEGACIÓN ---
    if (currentScreen == "STORE") {

        // Si estamos en la tienda, dibujamos todo tu código original
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentPadding = PaddingValues(bottom = 0.dp)
        ) {
            // 1. CABECERA: Buscador, Categorías, etc.
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // ¡NUEVO!: Le pasamos el cable al TopNavBar para que avise cuando toquen el carrito
                    // ¡NUEVO!: Le pasamos el cable al TopNavBar para el perfil
                    TopNavBar(
                        onCartClick = { currentScreen = "CART" },
                        onProfileClick = { currentScreen = "AUTH" }
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
                    // CAMBIO: Usamos 'items' en lugar de escribir uno por uno
                    items(verticalProducts) { product ->
                        ProductCard(
                            product = product,
                            initialIsInCart = false,
                            modifier = Modifier.width(180.dp)
                        )
                    }
                }
            }
            // 3. TERCERA SECCIÓN (Tarjetas Apaisadas)
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
                        // CAMBIO: Ahora es dinámico desde Firebase
                        items(kitsProducts) { product ->
                            LandscapeProductCard(
                                name = product.name,
                                price = product.price,
                                modifier = Modifier // Puedes pasarle eventos aquí luego
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

        CartScreen(
            onBackClick = { currentScreen = "STORE" }
        )

    } else if (currentScreen == "AUTH") {

        // ¡DIBUJA LA PANTALLA DE LOGIN!
        AuthScreen(
            onClose = { currentScreen = "STORE" },
            onGoogleSignInClick = {
                println("Clic en Iniciar sesión con Google")
                // Aquí conectaremos Firebase más adelante
            }
        )

    }
}