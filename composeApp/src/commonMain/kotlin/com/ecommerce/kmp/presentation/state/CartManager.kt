package com.ecommerce.kmp.presentation.state

import com.ecommerce.kmp.domain.model.CartItem
import com.ecommerce.kmp.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object CartManager {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
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