package com.ecommerce.kmp.domain.model

import kotlinx.serialization.Serializable

/**
 * ============================================================================
 * 📦 ORDER DOMAIN ENTITY
 * ============================================================================
 * * @description
 * Central domain model representing a customer's purchase transaction.
 * Encapsulates the cart items, financial totals, tracking timestamp, and the
 * fulfillment lifecycle (`status`) alongside its payment proof (`voucherUrl`).
 * * @layer Domain / Model
 * ============================================================================
 */

@Serializable
data class Order(
    val id: String = "",
    val userId: String = "",
    val customerName: String = "",
    val totalAmount: Double = 0.0,
    val items: List<CartItem> = emptyList(),
    val status: String = "",
    val voucherUrl: String = "",
    val timestamp: Long = 0L
)