package com.ecommerce.kmp.data.repository

import com.ecommerce.kmp.domain.model.Product
import com.ecommerce.kmp.domain.repository.KitRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ============================================================================
 * 🎁 MOCK KIT REPOSITORY (IN-MEMORY DATABASE)
 * ============================================================================
 * * @description
 * This class provides a concrete, in-memory implementation of the
 * `KitRepository` interface. It serves as a mock local database exclusively
 * dedicated to managing "Promotional Kits" and bundle offers, pre-populated
 * with 6 aesthetic collections.
 * * Key Architecture Features:
 * - Simulated Latency: Employs `delay()` within suspend functions to replicate
 * real-world network operations for UI loading state verification.
 * - Reactive State Management: Backed by a `MutableStateFlow` to ensure that
 * when an administrator adds a new Kit (`addKit`), the horizontally scrolling
 * `LazyRow` in the consumer Home screen automatically recomposes with the new data.
 * * 🔌 NOTE FOR BACKEND TEAM:
 * This repository is constructed strictly for UI prototyping and offline portfolio
 * demonstration. For a production release, replace this with a remote data
 * source implementation (e.g., Ktor or Retrofit) capable of handling multipart
 * form data for kit image uploads.
 * * @layer Data / Repository
 * ============================================================================
 */

class MockKitRepositoryImpl : KitRepository {

    // Simula nuestra base de datos en la nube exclusiva para Kits
    private val mockKits = mutableListOf(
        Product(
            id = "25",
            name = "Kit Regalo Energía Cítrica",
            price = 155.0,
            category = "Kits",
            description = "El regalo perfecto para revitalizar los sentidos. Incluye perfume, crema de manos y loción corporal con notas vibrantes de mandarina en una hermosa caja de edición especial.",
            imageUrl = "img_kit_citrico"
        ),
        Product(
            id = "26",
            name = "Kit Spa Botánico Premium",
            price = 270.0,
            category = "Kits",
            description = "Una experiencia de spa de lujo en casa. Colección completa de aceites, sales de baño y lociones en envases de vidrio ecológico para una relajación absoluta.",
            imageUrl = "img_kit_botanico"
        ),
        Product(
            id = "27",
            name = "Kit Rutina Diaria Minimalista",
            price = 140.0,
            category = "Kits",
            description = "Todo lo que necesitas para tu día a día. Gel limpiador, tónico, crema hidratante y mascarilla con fórmulas limpias y envases altamente estéticos.",
            imageUrl = "img_kit_minimalista"
        ),
        Product(
            id = "28",
            name = "Kit Reparador de Centella",
            price = 180.0,
            category = "Kits",
            description = "La línea definitiva para pieles sensibles. Contiene limpiador, sérum y cremas ricas en Centella Asiática para restaurar y fortalecer la barrera cutánea.",
            imageUrl = "img_kit_centella"
        ),
        Product(
            id = "29",
            name = "Kit Resplandor de Higo",
            price = 165.0,
            category = "Kits",
            description = "Rutina iluminadora completa enriquecida con extracto de higo natural. Exfolia suavemente y devuelve el brillo natural y la vitalidad a los rostros apagados.",
            imageUrl = "img_kit_higo"
        ),
        Product(
            id = "30",
            name = "Colección Rosas y Frutos Rojos",
            price = 210.0,
            category = "Kits",
            description = "El lujo máximo en cuidado facial. Infusiones de rosas reales y frutos rojos antioxidantes que combaten el envejecimiento prematuro dejando una tez de porcelana.",
            imageUrl = "img_kit_rosas"
        )
    )

    private val kitsFlow = MutableStateFlow<List<Product>>(mockKits.toList())

    override suspend fun getKits(): List<Product> {
        delay(800)
        return kitsFlow.value
    }

    override fun observeKits(): Flow<List<Product>> {
        return kitsFlow.asStateFlow()
    }

    override suspend fun addKit(kit: Product, imageBytes: ByteArray?): Boolean {
        delay(1000)

        val newKit = kit.copy(
            id = (mockKits.size + 25).toString(),
            imageUrl = "img_kit_citrico"
        )

        mockKits.add(newKit)
        kitsFlow.value = mockKits.toList()

        return true
    }
}