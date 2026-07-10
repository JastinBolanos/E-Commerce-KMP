package com.ecommerce.kmp.presentation.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecommerce.kmp.domain.model.CartItem
import com.ecommerce.kmp.presentation.components.BackHandler
import com.ecommerce.kmp.presentation.components.CategoryFilter
import com.ecommerce.kmp.presentation.components.FooterSection
import com.ecommerce.kmp.presentation.components.LandscapeProductCard
import com.ecommerce.kmp.presentation.components.ProductCard
import com.ecommerce.kmp.presentation.components.SearchInput
import com.ecommerce.kmp.presentation.components.TestimonialBanner
import com.ecommerce.kmp.presentation.components.TopNavBar
import com.ecommerce.kmp.presentation.features.cart.CartScreen
import com.ecommerce.kmp.presentation.features.checkout.CheckoutScreen
import com.ecommerce.kmp.presentation.features.checkout.CheckoutViewModel
import com.ecommerce.kmp.presentation.state.CartManager

/**
 * ============================================================================
 * 🏠 HOME SCREEN & CUSTOMER FLOW ORCHESTRATOR
 * ============================================================================
 * * @description
 * This is the primary UI container for the Customer-facing application.
 * It acts as a nested state-machine managing the shopping journey:
 * `STORE` (Browsing) <-> `CART` (Review) <-> `CHECKOUT` (Payment).
 * * It handles complex UI behaviors such as:
 * - Dynamic Category filtering with conditional spacer rendering.
 * - Horizontal scrolling lists for Products and Kits.
 * - 'Direct Purchase' (Buy Now) vs 'Cart Checkout' logic routing.
 * * 🔌 NOTE FOR BACKEND / UI INTEGRATION TEAM:
 * This screen purely observes `StateFlows` exposed by `HomeViewModel` and
 * `CheckoutViewModel`. If the backend API changes the structure of `Product`
 * or `Kit` objects, or adds new categories, only the Repositories need updating.
 * The UI automatically reacts and recomposes.
 * For deep linking (e.g., opening the app directly to a specific product),
 * the `currentScreen` state mechanism here should be replaced or integrated
 * with the native routing library (e.g., Compose Navigation).
 * * @layer Presentation / Features / Home
 * ============================================================================
 */

@Composable
fun HomeScreen(
    onAdminClick: () -> Unit,
    onAuthClick: () -> Unit,
    viewModel: HomeViewModel,
    checkoutViewModel: CheckoutViewModel
) {
    var isSearchFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var currentScreen by remember { mutableStateOf("STORE") }
    var directPurchaseItem by remember { mutableStateOf<CartItem?>(null) }

    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val rawKits by viewModel.kits.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val verticalProducts = filteredProducts

    val kitsProducts = rawKits.filter { kit ->
        val matchesQuery = kit.name.contains(searchQuery, ignoreCase = true) ||
                kit.description.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == "Todos" || selectedCategory.equals("Kits", ignoreCase = true)

        matchesQuery && matchesCategory
    }

    when (currentScreen) {
        "STORE" -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { focusManager.clearFocus() }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 0.dp)
                ) {
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            TopNavBar(
                                onCartClick = { currentScreen = "CART" },
                                onProfileClick = { onAuthClick() }
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            SearchInput(
                                query = searchQuery,
                                onQueryChange = { viewModel.updateSearchQuery(it) },
                                isFocused = isSearchFocused,
                                onFocusChange = { isSearchFocused = it }
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
                        if (verticalProducts.isNotEmpty()) {
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
                    }

                    item {
                        if (kitsProducts.isNotEmpty()) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                val isKitsCategory = selectedCategory.equals("Kits", ignoreCase = true)

                                if (!isKitsCategory) {
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(
                                        text = "Kits y Promociones",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                }

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
        }

        "CART" -> {
            BackHandler { currentScreen = "STORE" }

            CartScreen(
                onBackClick = { currentScreen = "STORE" },
                onProceedToCheckoutClick = {
                    directPurchaseItem = null
                    currentScreen = "CHECKOUT"
                },
                onDirectPurchaseClick = { itemToBuy ->
                    directPurchaseItem = itemToBuy
                    currentScreen = "CHECKOUT"
                }
            )
        }

        "CHECKOUT" -> {
            BackHandler {
                if (directPurchaseItem != null) {
                    directPurchaseItem = null
                    currentScreen = "CART"
                } else {
                    currentScreen = "CART"
                }
            }

            val isLoading by checkoutViewModel.isLoading.collectAsState()
            val orderSuccess by checkoutViewModel.orderSuccess.collectAsState()
            val paymentSettings by checkoutViewModel.paymentSettings.collectAsState()
            val itemsToBuy = if (directPurchaseItem != null) listOf(directPurchaseItem!!) else CartManager.cartItems.value

            val total = itemsToBuy.sumOf { item ->
                item.product.price * item.quantity
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
                qrUrl = "",
                phoneNumber = paymentSettings.yapeNumber,
                onBackClick = {
                    if (directPurchaseItem != null) {
                        directPurchaseItem = null
                        currentScreen = "CART"
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