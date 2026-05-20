package com.remedioz.natura.domain.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object CartManager {

    // Lista interna mutable y privada que maneja los elementos del carrito
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())

    // Estado público e inmutable expuesto hacia la UI (Reactivo)
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    /**
     * Agrega un producto al carrito.
     * Si el producto ya existe en la carretilla, incrementa su cantidad automáticamente.
     */
    fun addProduct(product: Product, quantity: Int) {
        val currentList = _cartItems.value.toMutableList()
        val existingItemIndex = currentList.indexOfFirst { it.product.id == product.id }

        if (existingItemIndex != -1) {
            // Regla de negocio: Si ya está en el carrito, se actualiza sumando las cantidades
            val existingItem = currentList[existingItemIndex]
            currentList[existingItemIndex] = existingItem.copy(
                quantity = existingItem.quantity + quantity
            )
        } else {
            // Si es un producto nuevo en la carretilla, lo añadimos directamente
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