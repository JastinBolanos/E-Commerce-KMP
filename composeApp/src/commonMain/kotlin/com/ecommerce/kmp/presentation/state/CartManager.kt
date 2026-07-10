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

    // --- DATOS DE DEMOSTRACIÓN PARA UI/UX ---
    private val demoItems = listOf(
        CartItem(
            product = Product(
                id = "demo_1",
                name = "Sérum Iluminador Pink Peptide",
                price = 85.00,
                category = "Tratamientos",
                description = "El secreto de la cosmética coreana. Péptidos concentrados que aportan un brillo de cristal y firmeza instantánea a tu rostro.",
                imageUrl = "img_serum_pink_peptide"
            ),
            quantity = 1
        ),
        CartItem(
            product = Product(
                id = "demo_2",
                name = "Eau de Parfum Brisa Oceánica",
                price = 130.00,
                category = "Belleza",
                description = "Frescura pura envasada. Notas de flor de loto, jazmín de agua y brisa marina que te envuelven en una sensación de limpieza y paz durante todo el día.",
                imageUrl = "img_perfume_azul"
            ),
            quantity = 1
        ),
        CartItem(
            product = Product(
                id = "demo_3",
                name = "Aceite Esencial de Argán Puro",
                price = 58.00,
                category = "Cuidados",
                description = "El \"oro líquido\" para tu rutina. Un aceite puro multiusos ideal para hidratar profundamente la piel y revitalizar las puntas secas del cabello.",
                imageUrl = "img_aceite_dorado"
            ),
            quantity = 1
        ),
        CartItem(
            product = Product(
                id = "demo_4",
                name = "Colección Rosas y Frutos Rojos",
                price = 210.00,
                category = "Kits",
                description = "El lujo máximo en cuidado facial. Infusiones de rosas reales y frutos rojos antioxidantes que combaten el envejecimiento prematuro dejando una tez de porcelana.",
                imageUrl = "img_kit_rosas"
            ),
            quantity = 1
        )
    )

    private val _cartItems = MutableStateFlow<List<CartItem>>(demoItems)
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    /**
     * Agrega un producto al carrito.
     * Si el producto ya existe en la carretilla, incrementa su cantidad automáticamente.
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
     * Elimina un producto por completo del carrito mediante su ID.
     */
    fun removeProduct(productId: String) {
        val currentList = _cartItems.value.toMutableList()
        currentList.removeAll { it.product.id == productId }
        _cartItems.value = currentList
    }

    /**
     * Vacía la carretilla por completo tras finalizar una compra exitosa.
     */
    fun clearCart() {
        _cartItems.value = emptyList()
    }
}