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

    // 1. Productos de nuestro nuevo catálogo para los pedidos iniciales
    private val dummyProduct1 = Product(
        id = "13",
        name = "Aceite Esencial de Argán Puro",
        price = 58.0,
        category = "Cuidados",
        description = "El \"oro líquido\" para tu rutina. Un aceite puro multiusos ideal para hidratar profundamente la piel y revitalizar las puntas secas del cabello.",
        imageUrl = "img_aceite_dorado"
    )

    private val dummyProduct2 = Product(
        id = "26",
        name = "Kit Spa Botánico Premium",
        price = 195.0,
        category = "Kits",
        description = "Una experiencia de spa de lujo en casa. Colección completa de aceites, sales de baño y lociones en envases de vidrio ecológico para una relajación absoluta.",
        imageUrl = "img_kit_botanico"
    )

    // 2. Base de datos en RAM con pedidos de prueba
    private val mockOrders = mutableListOf(
        Order(
            id = "ORD-77382",
            userId = "USER-ANONYMOUS",
            customerName = "Usuario Portafolio",
            items = listOf(CartItem(dummyProduct1, 2)), // Compró 2 aceites (2 * 58.0)
            totalAmount = 116.0,
            voucherUrl = "url_voucher_1",
            status = "Entregado",
            timestamp = 1704067200000L
        ),
        Order(
            id = "ORD-99124",
            userId = "USER-ANONYMOUS",
            customerName = "Usuario Portafolio",
            items = listOf(CartItem(dummyProduct2, 1)), // Compró 1 Kit (1 * 195.0)
            totalAmount = 195.0,
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