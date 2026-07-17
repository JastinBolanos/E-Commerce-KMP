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

    // --- 1. DEMO PRODUCTS FOR ORDERS ---

    private val productArgan = Product(
        id = "13",
        name = "Pure Argan Essential Oil",
        price = 58.0,
        category = "Care",
        description = "The \"liquid gold\" for your routine. A multi-purpose pure oil ideal for deeply hydrating the skin and revitalizing dry hair ends.",
        imageUrl = "img_aceite_dorado"
    )

    private val productKitBotanico = Product(
        id = "26",
        name = "Premium Botanical Spa Kit",
        price = 270.0,
        category = "Kits",
        description = "A luxury spa experience at home. Complete collection of oils, bath salts, and lotions in eco-friendly glass packaging for absolute relaxation.",
        imageUrl = "img_kit_botanico"
    )

    private val productSerumPink = Product(
        id = "1",
        name = "Pink Peptide Illuminating Serum",
        price = 85.0,
        category = "Skin",
        description = "The secret of Korean cosmetics. Concentrated peptides that provide crystal shine and instant firmness to your face.",
        imageUrl = "img_serum_pink_peptide"
    )

    private val productPerfumeOceano = Product(
        id = "10",
        name = "Ocean Breeze Eau de Parfum",
        price = 130.0,
        category = "Beauty",
        description = "Pure freshness bottled. Notes of lotus flower, water jasmine, and sea breeze that envelop you in a feeling of cleanliness and peace all day long.",
        imageUrl = "img_perfume_azul"
    )

    private val productKitCitrico = Product(
        id = "25",
        name = "Citrus Energy Gift Kit",
        price = 155.0,
        category = "Kits",
        description = "The perfect gift to revitalize the senses. Includes perfume, hand cream, and body lotion with vibrant tangerine notes in a beautiful special edition box.",
        imageUrl = "img_kit_citrico"
    )

    // --- 2. RAM DATABASE WITH 5 DIFFERENT STATES AND FICTIONAL CUSTOMERS ---

    private val mockOrders = mutableListOf(
        // STATE 1: Delivered
        Order(
            id = "ORD-77382",
            userId = "USER-LAURA",
            customerName = "Laura Veracruz",
            items = listOf(CartItem(productArgan, 2)),
            totalAmount = 116.0,
            voucherUrl = "url_voucher_1",
            status = "Delivered",
            timestamp = getCurrentTimeMillis() - 864000000L
        ),
        // STATE 2: In Transit
        Order(
            id = "ORD-88102",
            userId = "USER-ANDREA",
            customerName = "Andrea Castañeda",
            items = listOf(CartItem(productKitCitrico, 1)),
            totalAmount = 155.0,
            voucherUrl = "url_voucher_2",
            status = "In Transit",
            timestamp = getCurrentTimeMillis() - 172800000L
        ),
        // STATE 3: Preparing Package
        Order(
            id = "ORD-92034",
            userId = "USER-ISABELLA",
            customerName = "Isabella Santillán",
            items = listOf(CartItem(productPerfumeOceano, 2)),
            totalAmount = 260.0,
            voucherUrl = "url_voucher_3",
            status = "Preparing Package",
            timestamp = getCurrentTimeMillis() - 86400000L
        ),
        // STATE 4: Approved
        Order(
            id = "ORD-98011",
            userId = "USER-CAMILA",
            customerName = "Camila de la Fuente",
            items = listOf(CartItem(productSerumPink, 1)),
            totalAmount = 85.0,
            voucherUrl = "url_voucher_4",
            status = "Approved",
            timestamp = getCurrentTimeMillis() - 3600000L
        ),
        // STATE 5: Pending (The most recent)
        Order(
            id = "ORD-99124",
            userId = "USER-LAURA",
            customerName = "Laura Veracruz",
            items = listOf(CartItem(productKitBotanico, 1)),
            totalAmount = 270.0,
            voucherUrl = "url_voucher_5",
            status = "Pending",
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