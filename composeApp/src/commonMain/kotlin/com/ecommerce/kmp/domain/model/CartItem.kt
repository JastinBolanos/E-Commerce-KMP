package com.ecommerce.kmp.domain.model

import kotlinx.serialization.Serializable

/**
 * ============================================================================
 * 🛒 CART ITEM DOMAIN ENTITY
 * ============================================================================
 * * @description
 * Represents a quantified product within a shopping session or order.
 * Fully serializable for local persistence and network transit.
 * * @layer Domain / Model
 * ============================================================================
 */

@Serializable
data class CartItem(
    val product: Product,
    val quantity: Int
)