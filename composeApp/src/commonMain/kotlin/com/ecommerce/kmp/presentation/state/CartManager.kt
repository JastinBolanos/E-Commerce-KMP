package com.ecommerce.kmp.presentation.state

import com.ecommerce.kmp.domain.model.CartItem
import com.ecommerce.kmp.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ============================================================================
 * 🛒 GLOBAL CART STATE MANAGER (IN-MEMORY SINGLETON)
 * ============================================================================
 * * @description
 * This object acts as a global Singleton managing the Shopping Cart's reactive
 * state via Kotlin Coroutines `StateFlow`. It handles the core business logic
 * for adding, updating quantities, removing, and clearing items synchronously
 * across the entire application lifecycle.
 * * 🔌 NOTE FOR BACKEND / DATA PERSISTENCE TEAM:
 * Currently, the cart state is purely in-memory (RAM) and pre-populated with
 * demo data for UI/UX showcase purposes. If the application is killed, the
 * cart data is lost.
 * For a production environment, this Singleton pattern should be refactored
 * into a `CartRepository` interface managed by a Dependency Injection framework
 * (like Koin or Dagger). The local state should be persisted using a local DB
 * (SQLDelight/Room) and/or synchronized remotely via a backend Cart API.
 * * @layer Presentation / Global State
 * ============================================================================
 */

object CartManager {

    // --- DEMO DATA FOR UI/UX ---
    private val demoItems = listOf(
        CartItem(
            product = Product(
                id = "demo_1",
                name = "Pink Peptide Illuminating Serum",
                price = 85.00,
                category = "Treatments",
                description = "The secret of Korean cosmetics. Concentrated peptides that provide crystal shine and instant firmness to your face.",
                imageUrl = "img_serum_pink_peptide"
            ),
            quantity = 1
        ),
        CartItem(
            product = Product(
                id = "demo_2",
                name = "Ocean Breeze Eau de Parfum",
                price = 130.00,
                category = "Beauty",
                description = "Pure freshness bottled. Notes of lotus flower, water jasmine, and sea breeze that envelop you in a feeling of cleanliness and peace all day long.",
                imageUrl = "img_perfume_azul"
            ),
            quantity = 1
        ),
        CartItem(
            product = Product(
                id = "demo_3",
                name = "Pure Argan Essential Oil",
                price = 58.00,
                category = "Care",
                description = "The \"liquid gold\" for your routine. A multi-purpose pure oil ideal for deeply hydrating the skin and revitalizing dry hair ends.",
                imageUrl = "img_aceite_dorado"
            ),
            quantity = 1
        ),
        CartItem(
            product = Product(
                id = "demo_4",
                name = "Roses and Red Fruits Collection",
                price = 210.00,
                category = "Kits",
                description = "The ultimate luxury in facial care. Infusions of real roses and antioxidant red fruits that combat premature aging, leaving a porcelain complexion.",
                imageUrl = "img_kit_rosas"
            ),
            quantity = 1
        )
    )

    private val _cartItems = MutableStateFlow<List<CartItem>>(demoItems)
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    /**
     * Adds a product to the cart.
     * If the product already exists in the cart, it automatically increments its quantity.
     */
    fun addProduct(product: Product, quantity: Int) {
        val currentList = _cartItems.value.toMutableList()
        val existingItemIndex = currentList.indexOfFirst { it.product.id == product.id }

        if (existingItemIndex != -1) {
            val existingItem = currentList[existingItemIndex]
            currentList[existingItemIndex] = existingItem.copy(
                quantity = existingItem.quantity + quantity
            )
        } else {
            currentList.add(CartItem(product, quantity))
        }

        _cartItems.value = currentList
    }

    /**
     * Completely removes a product from the cart by its ID.
     */
    fun removeProduct(productId: String) {
        val currentList = _cartItems.value.toMutableList()
        currentList.removeAll { it.product.id == productId }
        _cartItems.value = currentList
    }

    /**
     * Completely empties the cart after a successful purchase.
     */
    fun clearCart() {
        _cartItems.value = emptyList()
    }
}