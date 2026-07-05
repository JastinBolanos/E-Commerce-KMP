package com.ecommerce.kmp.data.repository

import com.ecommerce.kmp.domain.model.Order
import com.ecommerce.kmp.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockOrderRepositoryImpl : OrderRepository {

    private val ordersFlow = MutableStateFlow<List<Order>>(emptyList())

    override fun observeOrders(): Flow<List<Order>> {
        return ordersFlow.asStateFlow()
    }

    override suspend fun updateOrderStatus(orderId: String, newStatus: String): Boolean {
        // En un mock, aquí podrías simular la actualización del estado
        return true
    }
}