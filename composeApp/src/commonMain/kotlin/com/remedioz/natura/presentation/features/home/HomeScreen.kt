package com.remedioz.natura.presentation.features.home

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
import com.remedioz.natura.presentation.components.CategoryFilter
import com.remedioz.natura.presentation.components.FooterSection
import com.remedioz.natura.presentation.components.LandscapeProductCard
import com.remedioz.natura.presentation.components.ProductCard
import com.remedioz.natura.presentation.components.SearchInput
import com.remedioz.natura.presentation.components.TestimonialBanner
import com.remedioz.natura.presentation.components.TopNavBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.remedioz.natura.presentation.features.cart.CartScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.items
import com.remedioz.natura.presentation.state.CartManager
import com.remedioz.natura.presentation.features.checkout.CheckoutScreen
import com.remedioz.natura.presentation.features.checkout.CheckoutViewModel

/**
 * Contenedor principal de la Tienda (UI Host).
 * Actúa como el consumidor final del [HomeViewModel], observando el estado reactivo
 * (Single Source of Truth) y distribuyéndolo a sus componentes hijos (State Hoisting).
 * Implementa un enrutador interno ligero de memoria local para alternar entre el catálogo y el carrito.
 *
 * @param onAdminClick Delegación de navegación hacia el módulo de administración protegido.
 * @param onAuthClick Delegación de navegación hacia el flujo de autenticación (SSO).
 * @param viewModel Orquestador de la lógica de negocio y proveedor del catálogo filtrado.
 */
@Composable
fun HomeScreen(
    onAdminClick: () -> Unit,
    onAuthClick: () -> Unit,
    viewModel: HomeViewModel,
    checkoutViewModel: CheckoutViewModel
) {
    var currentScreen by remember { mutableStateOf("STORE") }

    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val kitsProducts = filteredProducts.filter { it.category.equals("Kits", ignoreCase = true) }
    val verticalProducts = filteredProducts.filter { !it.category.equals("Kits", ignoreCase = true) }

    if (currentScreen == "STORE") {
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(Color.White),
            contentPadding = PaddingValues(bottom = 0.dp)
        ) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TopNavBar(onCartClick = { currentScreen = "CART" }, onProfileClick = { onAuthClick() })
                    Spacer(modifier = Modifier.height(8.dp))

                    SearchInput(
                        query = searchQuery,
                        onQueryChange = { viewModel.updateSearchQuery(it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    CategoryFilter(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { viewModel.updateCategory(it) }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(verticalProducts) { product ->
                        ProductCard(
                            product = product,
                            modifier = Modifier.width(180.dp)
                        )
                    }
                }
            }

            item {
                if (kitsProducts.isNotEmpty()) {
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
                                LandscapeProductCard(product = product, modifier = Modifier)
                            }
                        }
                    }
                }
            }

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
            onBackClick = { currentScreen = "STORE" },
            onProceedToCheckoutClick = { currentScreen = "CHECKOUT" }
        )
    } else if (currentScreen == "CHECKOUT") {

        val total = CartManager.cartItems.value.sumOf { item ->
            (item.product.price.toDoubleOrNull() ?: 0.0) * item.quantity
        }

        CheckoutScreen(
            totalAmount = total,
            onBackClick = { currentScreen = "CART" },
            onConfirmOrder = { voucherBytes ->
                checkoutViewModel.processOrder(
                    userId = "USER-ANONYMOUS", // TODO: Conectar con Auth real en el futuro
                    customerName = "Cliente de App",
                    cartItems = CartManager.cartItems.value,
                    total = total,
                    voucherBytes = voucherBytes
                )
                // Opcional por ahora: limpiar el carrito y volver al inicio
                CartManager.clearCart()
                currentScreen = "STORE"
            }
        )
    }
}