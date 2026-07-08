package com.ecommerce.kmp.data.repository

import com.ecommerce.kmp.data.platform.getCurrentTimeMillis
import com.ecommerce.kmp.domain.model.CartItem
import com.ecommerce.kmp.domain.model.Order
import com.ecommerce.kmp.domain.model.Product
import com.ecommerce.kmp.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockOrderRepositoryImpl : OrderRepository {

    // 1. un par de productos de tu catálogo para los pedidos iniciales
    private val dummyProduct1 = Product(
        id = "1",
        name = "Aceite Esencial de Lavanda",
        price = 45.0,
        category = "Cuidados",
        description = "Extracto puro 100% natural para aliviar el estrés y mejorar el sueño nocturno.",
        imageUrl = "img_lavanda"
    )

    private val dummyProduct2 = Product(
        id = "4",
        name = "Kit Bienestar Integral",
        price = 85.0,
        category = "Kits",
        description = "El combo perfecto: Aceite de lavanda, infusión relajante y crema hidratante a un precio especial.",
        imageUrl = "img_placeholder"
    )

    // 2. base de datos en RAM con pedidos de prueba
    private val mockOrders = mutableListOf(
        Order(
            id = "ORD-77382",
            userId = "USER-ANONYMOUS",
            customerName = "Usuario Portafolio",
            items = listOf(CartItem(dummyProduct1, 2)),
            totalAmount = 90.0,
            voucherUrl = "url_voucher_1",
            status = "Entregado",
            timestamp = 1704067200000L
        ),
        Order(
            id = "ORD-99124",
            userId = "USER-ANONYMOUS",
            customerName = "Usuario Portafolio",
            items = listOf(CartItem(dummyProduct2, 1)),
            totalAmount = 85.0,
            voucherUrl = "url_voucher_2",
            status = "Pendiente",
            timestamp = getCurrentTimeMillis()
        )
    )

    private val ordersFlow = MutableStateFlow<List<Order>>(mockOrders.toList())

    override fun observeOrders(): Flow<List<Order>> {
        return ordersFlow.asStateFlow()
    }

    override suspend fun updateOrderStatus(orderId: String, newStatus: String): Boolean {
        val index = mockOrders.indexOfFirst { it.id == orderId }

        if (index != -1) {
            val updatedOrder = mockOrders[index].copy(status = newStatus)
            mockOrders[index] = updatedOrder
            ordersFlow.value = mockOrders.toList()
            return true
        }
        return false
    }
}