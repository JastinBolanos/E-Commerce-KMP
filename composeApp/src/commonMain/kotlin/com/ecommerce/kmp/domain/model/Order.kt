package com.ecommerce.kmp.domain.model

import kotlinx.serialization.Serializable

/**
 * Representa un pedido realizado por un cliente.
 * Contiene el estado del pago y la referencia al voucher.
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