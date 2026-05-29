package com.remedioz.natura.domain.model

/**
 * Representa un pedido realizado por un cliente.
 * Contiene el estado del pago y la referencia al voucher (Yape/Plin).
 */
data class Order(
    val id: String = "",
    val userId: String = "",
    val customerName: String = "",
    val items: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val voucherUrl: String = "",
    val status: String = "PENDIENTE",
    val timestamp: Long = 0L
)