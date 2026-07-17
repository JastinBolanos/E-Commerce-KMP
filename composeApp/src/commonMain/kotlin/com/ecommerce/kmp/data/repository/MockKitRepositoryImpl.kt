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

    // Simulates our cloud database exclusively for Kits
    private val mockKits = mutableListOf(
        Product(
            id = "25",
            name = "Citrus Energy Gift Kit",
            price = 155.0,
            category = "Kits",
            description = "The perfect gift to revitalize the senses. Includes perfume, hand cream, and body lotion with vibrant tangerine notes in a beautiful special edition box.",
            imageUrl = "img_kit_citrico"
        ),
        Product(
            id = "26",
            name = "Premium Botanical Spa Kit",
            price = 270.0,
            category = "Kits",
            description = "A luxury spa experience at home. Complete collection of oils, bath salts, and lotions in eco-friendly glass packaging for absolute relaxation.",
            imageUrl = "img_kit_botanico"
        ),
        Product(
            id = "27",
            name = "Minimalist Daily Routine Kit",
            price = 140.0,
            category = "Kits",
            description = "Everything you need for your day to day. Cleansing gel, toner, moisturizing cream, and mask with clean formulas and highly aesthetic packaging.",
            imageUrl = "img_kit_minimalista"
        ),
        Product(
            id = "28",
            name = "Centella Repair Kit",
            price = 180.0,
            category = "Kits",
            description = "The definitive line for sensitive skin. Contains cleanser, serum, and creams rich in Centella Asiatica to restore and strengthen the skin barrier.",
            imageUrl = "img_kit_centella"
        ),
        Product(
            id = "29",
            name = "Fig Radiance Kit",
            price = 165.0,
            category = "Kits",
            description = "Complete illuminating routine enriched with natural fig extract. Gently exfoliates and restores natural shine and vitality to dull faces.",
            imageUrl = "img_kit_higo"
        ),
        Product(
            id = "30",
            name = "Roses and Red Fruits Collection",
            price = 210.0,
            category = "Kits",
            description = "The ultimate luxury in facial care. Infusions of real roses and antioxidant red fruits that combat premature aging, leaving a porcelain complexion.",
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