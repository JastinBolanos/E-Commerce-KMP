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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import com.remedioz.natura.presentation.components.CategoryFilter
import com.remedioz.natura.presentation.components.FooterSection
import com.remedioz.natura.presentation.components.LandscapeProductCard
import com.remedioz.natura.presentation.components.ProductCard
import com.remedioz.natura.presentation.components.SearchInput
import com.remedioz.natura.presentation.components.TestimonialBanner
import com.remedioz.natura.presentation.components.TopNavBar
import com.remedioz.natura.presentation.features.cart.CartScreen
import com.remedioz.natura.presentation.components.BackHandler
import com.remedioz.natura.presentation.state.CartManager
import com.remedioz.natura.domain.model.CartItem
import com.remedioz.natura.presentation.features.checkout.CheckoutScreen
import com.remedioz.natura.presentation.features.checkout.CheckoutViewModel

@Composable
fun HomeScreen(
    onAdminClick: () -> Unit,
    onAuthClick: () -> Unit,
    viewModel: HomeViewModel,
    checkoutViewModel: CheckoutViewModel
) {
    var currentScreen by remember { mutableStateOf("STORE") }
    var directPurchaseItem by remember { mutableStateOf<CartItem?>(null) }
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val kitsProducts = filteredProducts.filter { it.category.equals("Kits", ignoreCase = true) }
    val verticalProducts =
        filteredProducts.filter { !it.category.equals("Kits", ignoreCase = true) }

    when (currentScreen) {
        "STORE" -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize().background(Color.White),
                contentPadding = PaddingValues(bottom = 0.dp)
            ) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        TopNavBar(
                            onCartClick = { currentScreen = "CART" },
                            onProfileClick = { onAuthClick() })
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
                                modifier = Modifier.width(180.dp),
                                onBuyNowClick = { qty ->
                                    directPurchaseItem = CartItem(product = product, quantity = qty)
                                    currentScreen = "CHECKOUT"
                                }
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
                                    LandscapeProductCard(
                                        product = product,
                                        modifier = Modifier,
                                        onBuyNowClick = { qty ->
                                            directPurchaseItem = CartItem(product = product, quantity = qty)
                                            currentScreen = "CHECKOUT"
                                        }
                                    )
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
        }

        "CART" -> {
            BackHandler { currentScreen = "STORE" }

            CartScreen(
                onBackClick = { currentScreen = "STORE" },
                onProceedToCheckoutClick = {
                    directPurchaseItem = null
                    currentScreen = "CHECKOUT"
                }
            )
        }

        "CHECKOUT" -> {
            BackHandler {
                if (directPurchaseItem != null) {
                    directPurchaseItem = null
                    currentScreen = "STORE"
                } else {
                    currentScreen = "CART"
                }
            }

            val isLoading by checkoutViewModel.isLoading.collectAsState()
            val orderSuccess by checkoutViewModel.orderSuccess.collectAsState()
            val paymentSettings by checkoutViewModel.paymentSettings.collectAsState()
            val itemsToBuy = if (directPurchaseItem != null) listOf(directPurchaseItem!!) else CartManager.cartItems.value

            val total = itemsToBuy.sumOf { item ->
                (item.product.price.toDoubleOrNull() ?: 0.0) * item.quantity
            }

            LaunchedEffect(orderSuccess) {
                if (orderSuccess) {
                    if (directPurchaseItem == null) {
                        CartManager.clearCart()
                    }
                    directPurchaseItem = null
                    checkoutViewModel.resetState()
                    currentScreen = "STORE"
                }
            }

            CheckoutScreen(
                totalAmount = total,
                isLoading = isLoading,
                qrUrl = paymentSettings.qrUrl,
                phoneNumber = paymentSettings.phoneNumber,
                onBackClick = {
                    if (directPurchaseItem != null) {
                        directPurchaseItem = null
                        currentScreen = "STORE"
                    } else {
                        currentScreen = "CART"
                    }
                },
                onConfirmOrder = { voucherBytes ->
                    checkoutViewModel.processOrder(
                        userId = "USER-ANONYMOUS",
                        customerName = "Cliente de App",
                        cartItems = itemsToBuy,
                        total = total,
                        voucherBytes = voucherBytes
                    )
                }
            )
        }
    }
}