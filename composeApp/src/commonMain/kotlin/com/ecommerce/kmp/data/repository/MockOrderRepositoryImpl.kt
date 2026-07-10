package com.ecommerce.kmp.data.repository

import com.ecommerce.kmp.data.platform.getCurrentTimeMillis
import com.ecommerce.kmp.domain.model.CartItem
import com.ecommerce.kmp.domain.model.Order
import com.ecommerce.kmp.domain.model.Product
import com.ecommerce.kmp.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ============================================================================
 * 🗄️ MOCK ORDER REPOSITORY (IN-MEMORY DATABASE)
 * ============================================================================
 * * @description
 * This class provides a concrete, in-memory implementation of the
 * `OrderRepository` interface. It serves as a mock local database, pre-populated
 * with a realistic dataset of orders spanning the entire fulfillment lifecycle
 * (Pending, Approved, Packing, In-Transit, Delivered) using relative timestamps.
 * * Key Architecture Features:
 * - Reactive Pipeline: Backed by a `MutableStateFlow` to guarantee that state
 * mutations (`updateOrderStatus`, `addOrder`) instantly trigger UI recompositions
 * across both the Customer and Admin dashboards.
 * - Immutable Updates: Utilizes Kotlin's data class `.copy()` mechanism to
 * safely transition order statuses without mutating the original object references.
 * * 🔌 NOTE FOR BACKEND TEAM:
 * This repository is designed for offline UI prototyping and portfolio showcase.
 * For production, this must be replaced with a real infrastructure implementation
 * (e.g., `FirestoreOrderRepositoryImpl` or `SupabaseOrderRepositoryImpl`) that
 * handles remote CRUD operations and listens to real-time WebSocket/Snapshot events.
 * * @layer Data / Repository
 * ============================================================================
 */

class MockOrderRepositoryImpl : OrderRepository {

    // --- 1. PRODUCTOS DE DEMOSTRACIÓN PARA LOS PEDIDOS ---

    private val productArgan = Product(
        id = "13",
        name = "Aceite Esencial de Argán Puro",
        price = 58.0,
        category = "Cuidados",
        description = "El \"oro líquido\" para tu rutina. Un aceite puro multiusos ideal para hidratar profundamente la piel y revitalizar las puntas secas del cabello.",
        imageUrl = "img_aceite_dorado"
    )

    private val productKitBotanico = Product(
        id = "26",
        name = "Kit Spa Botánico Premium",
        price = 270.0,
        category = "Kits",
        description = "Una experiencia de spa de lujo en casa. Colección completa de aceites, sales de baño y lociones en envases de vidrio ecológico para una relajación absoluta.",
        imageUrl = "img_kit_botanico"
    )

    private val productSerumPink = Product(
        id = "1",
        name = "Sérum Iluminador Pink Peptide",
        price = 85.0,
        category = "Piel",
        description = "El secreto de la cosmética coreana. Péptidos concentrados que aportan un brillo de cristal y firmeza instantánea a tu rostro.",
        imageUrl = "img_serum_pink_peptide"
    )

    private val productPerfumeOceano = Product(
        id = "10",
        name = "Eau de Parfum Brisa Oceánica",
        price = 130.0,
        category = "Belleza",
        description = "Frescura pura envasada. Notas de flor de loto, jazmín de agua y brisa marina que te envuelven en una sensación de limpieza y paz durante todo el día.",
        imageUrl = "img_perfume_azul"
    )

    private val productKitCitrico = Product(
        id = "25",
        name = "Kit Regalo Energía Cítrica",
        price = 155.0,
        category = "Kits",
        description = "El regalo perfecto para revitalizar los sentidos. Incluye perfume, crema de manos y loción corporal con notas vibrantes de mandarina en una hermosa caja de edición especial.",
        imageUrl = "img_kit_citrico"
    )

    // --- 2. BASE DE DATOS EN RAM CON 5 ESTADOS DIFERENTES Y CLIENTES FICTICIOS ---

    private val mockOrders = mutableListOf(
        // ESTADO 1: Entregado
        Order(
            id = "ORD-77382",
            userId = "USER-LAURA",
            customerName = "Laura Veracruz",
            items = listOf(CartItem(productArgan, 2)),
            totalAmount = 116.0,
            voucherUrl = "url_voucher_1",
            status = "Entregado",
            timestamp = getCurrentTimeMillis() - 864000000L
        ),
        // ESTADO 2: En Camino
        Order(
            id = "ORD-88102",
            userId = "USER-ANDREA",
            customerName = "Andrea Castañeda",
            items = listOf(CartItem(productKitCitrico, 1)),
            totalAmount = 155.0,
            voucherUrl = "url_voucher_2",
            status = "En Camino",
            timestamp = getCurrentTimeMillis() - 172800000L
        ),
        // ESTADO 3: Preparando Paquete
        Order(
            id = "ORD-92034",
            userId = "USER-ISABELLA",
            customerName = "Isabella Santillán",
            items = listOf(CartItem(productPerfumeOceano, 2)),
            totalAmount = 260.0,
            voucherUrl = "url_voucher_3",
            status = "Preparando Paquete",
            timestamp = getCurrentTimeMillis() - 86400000L
        ),
        // ESTADO 4: Aprobado
        Order(
            id = "ORD-98011",
            userId = "USER-CAMILA",
            customerName = "Camila de la Fuente",
            items = listOf(CartItem(productSerumPink, 1)),
            totalAmount = 85.0,
            voucherUrl = "url_voucher_4",
            status = "Aprobado",
            timestamp = getCurrentTimeMillis() - 3600000L
        ),
        // ESTADO 5: Pendiente (El más reciente)
        Order(
            id = "ORD-99124",
            userId = "USER-LAURA",
            customerName = "Laura Veracruz",
            items = listOf(CartItem(productKitBotanico, 1)),
            totalAmount = 270.0,
            voucherUrl = "url_voucher_5",
            status = "Pendiente",
            timestamp = getCurrentTimeMillis()
        )
    )

    private val ordersFlow = MutableStateFlow<List<Order>>(mockOrders.toList())

    override fun observeOrders(): Flow<List<Order>> {
        return ordersFlow.asStateFlow()
    }

    override suspend fun addOrder(order: Order): Boolean {
        mockOrders.add(0, order)
        ordersFlow.value = mockOrders.toList()
        return true
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