package com.ecommerce.kmp.domain.repository

import com.ecommerce.kmp.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun observeOrders(): Flow<List<Order>>
    suspend fun updateOrderStatus(orderId: String, newStatus: String): Boolean
}