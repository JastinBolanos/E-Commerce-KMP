package com.ecommerce.kmp.domain.repository

import com.ecommerce.kmp.domain.model.Order
import kotlinx.coroutines.flow.Flow

/**
 * ============================================================================
 * 📦 ORDER REPOSITORY PORTAL
 * ============================================================================
 * * @description
 * Domain-level contract for Order fulfillment and lifecycle tracking.
 * Provides a continuous stream of incoming purchases (`observeOrders`)
 * and state-machine mutation capabilities (`updateOrderStatus`).
 * * @layer Domain / Repository
 * ============================================================================
 */

interface OrderRepository {
    fun observeOrders(): Flow<List<Order>>
    suspend fun updateOrderStatus(orderId: String, newStatus: String): Boolean
    suspend fun addOrder(order: Order): Boolean
}