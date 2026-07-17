package com.ecommerce.kmp.data.repository

import com.ecommerce.kmp.domain.model.Product
import com.ecommerce.kmp.domain.repository.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ============================================================================
 * 🗄️ MOCK PRODUCT REPOSITORY (IN-MEMORY DATABASE)
 * ============================================================================
 * * @description
 * This class provides a concrete, in-memory implementation of the
 * `ProductRepository` interface. It serves as a mock local database,
 * pre-populated with a comprehensive catalog of 24 cosmetic products
 * across 4 domains (Piel, Belleza, Cuidados, Tratamientos).
 * * Key Architecture Features:
 * - Simulated Latency: Uses `delay()` in suspend functions to mimic
 * real-world network operations, ensuring UI loading states are testable.
 * - Reactive State: Backed by a `MutableStateFlow` to guarantee that any
 * mutations (like `addProduct`) instantly propagate to all observing
 * ViewModels via `observeProducts()`.
 * * 🔌 NOTE FOR BACKEND TEAM:
 * This file is strictly for prototyping and offline showcases. Before
 * production release, replace this implementation with a remote data
 * source (e.g., `KtorProductRepositoryImpl`) that performs actual HTTP
 * GET/POST requests and handles real image multipart uploads.
 * * @layer Data / Repository
 * ============================================================================
 */

class MockProductRepositoryImpl : ProductRepository {

    // Simulates our cloud database, but lives in RAM memory
    private val mockProducts = mutableListOf(

        // --- CATEGORY 1: SKIN ---
        Product(
            id = "1",
            name = "Pink Peptide Illuminating Serum",
            price = 85.0,
            category = "Skin",
            description = "The secret of Korean cosmetics. Concentrated peptides that provide crystal shine and instant firmness to your face.",
            imageUrl = "img_serum_pink_peptide"
        ),
        Product(
            id = "2",
            name = "Dolkong Nourishing Facial Cream",
            price = 68.0,
            category = "Skin",
            description = "Rich texture enriched with pure soy extracts. Essential nutrition to restore the skin barrier of dry skin.",
            imageUrl = "img_crema_dolkong"
        ),
        Product(
            id = "3",
            name = "Rose Revitalizing Serum",
            price = 75.0,
            category = "Skin",
            description = "Concentrated formula with botanical rose oils. Deeply hydrates and leaves a luminous finish without a greasy feel.",
            imageUrl = "img_tonico_purificante"
        ),
        Product(
            id = "4",
            name = "Anti-Aging Night Cream",
            price = 95.0,
            category = "Skin",
            description = "Intensive fragrance-free night treatment. Repairs cellular tissue and visibly reduces fine lines while you sleep.",
            imageUrl = "img_crema_noche"
        ),
        Product(
            id = "5",
            name = "Chamomile Cleansing Soap",
            price = 25.0,
            category = "Skin",
            description = "Ultra-gentle solid facial cleanser. Calms redness and irritation thanks to the anti-inflammatory properties of natural chamomile.",
            imageUrl = "img_jabon_manzanilla"
        ),
        Product(
            id = "6",
            name = "Clay Purifying Soap",
            price = 22.0,
            category = "Skin",
            description = "Ideal for combination or oily-prone skin. Green clay unclogs pores and controls excess facial sebum.",
            imageUrl = "img_jabon_arcilla"
        ),

        // --- CATEGORY 2: BEAUTY ---
        Product(
            id = "7",
            name = "Lemon and Coconut Lip Balm",
            price = 18.0,
            category = "Beauty",
            description = "Intense hydration with coconut butter and citrus extract. Protects your lips from the sun while you enjoy a refreshing tropical scent.",
            imageUrl = "img_balsamo_citrico"
        ),
        Product(
            id = "8",
            name = "Ruby Passion Botanical Perfume",
            price = 145.0,
            category = "Beauty",
            description = "An intense and sophisticated fragrance. Combines notes of wild red fruits, amber, and a touch of organic vanilla for very special occasions.",
            imageUrl = "img_perfume_rojo"
        ),
        Product(
            id = "9",
            name = "Silk Petal Multi-use Tint",
            price = 42.0,
            category = "Beauty",
            description = "Natural lip and cheek tint. Its ultra-light cream texture blends perfectly, providing a romantic color and velvety finish.",
            imageUrl = "img_tinte_seda"
        ),
        Product(
            id = "10",
            name = "Ocean Breeze Eau de Parfum",
            price = 130.0,
            category = "Beauty",
            description = "Pure freshness bottled. Notes of lotus flower, water jasmine, and sea breeze that envelop you in a feeling of cleanliness and peace all day long.",
            imageUrl = "img_perfume_azul"
        ),
        Product(
            id = "11",
            name = "Natural Glow Liquid Blush",
            price = 55.0,
            category = "Beauty",
            description = "A touch of healthy color that melts into your skin. Dewy finish, 100% vegan, highly pigmented, and long-lasting without clogging pores.",
            imageUrl = "img_rubor_liquido"
        ),
        Product(
            id = "12",
            name = "Chamomile Repair Balm",
            price = 20.0,
            category = "Beauty",
            description = "Expert care for chapped lips. Enriched with chamomile and sweet almond essential oils to repair, soothe, and protect from extreme cold.",
            imageUrl = "img_balsamo_herbal"
        ),

        // --- CATEGORY 3: CARE ---
        Product(
            id = "13",
            name = "Pure Argan Essential Oil",
            price = 58.0,
            category = "Care",
            description = "The \"liquid gold\" for your routine. A multi-purpose pure oil ideal for deeply hydrating the skin and revitalizing dry hair ends.",
            imageUrl = "img_aceite_dorado"
        ),
        Product(
            id = "14",
            name = "Sweet Vanilla Body Butter",
            price = 45.0,
            category = "Care",
            description = "A dessert for your skin. Ultra-nourishing body balm that melts on contact, leaving deep hydration and an irresistible scent.",
            imageUrl = "img_mantequilla_caramelo"
        ),
        Product(
            id = "15",
            name = "Botanical Healing Oil",
            price = 62.0,
            category = "Care",
            description = "Organic blend enriched with macadamia. Helps repair scars, smooth stretch marks, and significantly improve your skin's natural elasticity.",
            imageUrl = "img_aceite_macadamia"
        ),
        Product(
            id = "16",
            name = "Revitalizing Solid Shampoo",
            price = 35.0,
            category = "Care",
            description = "Eco-friendly, package-free alternative. Gently cleanses the scalp with botanical ingredients, generating a rich, purifying lather.",
            imageUrl = "img_shampoo_solido"
        ),
        Product(
            id = "17",
            name = "Luminous Body Scrub",
            price = 52.0,
            category = "Care",
            description = "Renew your body's texture in the shower. Fine crystals and essential oils that remove dead cells and leave your skin silky and perfumed.",
            imageUrl = "img_exfoliante_corporal"
        ),
        Product(
            id = "18",
            name = "Artisanal Oatmeal Soap",
            price = 18.0,
            category = "Care",
            description = "Handmade with whole oats and chamomile extract. Gently exfoliates and soothes the most sensitive skin, ideal for daily use.",
            imageUrl = "img_jabon_avena"
        ),

        // --- CATEGORY 4: TREATMENTS ---
        Product(
            id = "19",
            name = "Balancing Facial Essence",
            price = 65.0,
            category = "Treatments",
            description = "Essence treatment with a milky texture. Prepares the skin after cleansing, balancing pH and maximizing the absorption of your subsequent serums.",
            imageUrl = "img_esencia_lechosa"
        ),
        Product(
            id = "20",
            name = "Illuminating Facial Mask",
            price = 24.0,
            category = "Treatments",
            description = "Sheet mask enriched with Vitamin C and grapefruit extracts. A 15-minute flash treatment to revive and energize dull skin.",
            imageUrl = "img_mascarilla_citrica"
        ),
        Product(
            id = "21",
            name = "Hair Repair Treatment",
            price = 110.0,
            category = "Treatments",
            description = "Intensive reconstruction system for damaged hair. Repairs hair bonds from the inside out, restoring strength, softness, and shine.",
            imageUrl = "img_tratamiento_capilar"
        ),
        Product(
            id = "22",
            name = "Vitamin C Treatment Gel",
            price = 78.0,
            category = "Treatments",
            description = "Antioxidant concentrate in a gel-serum texture. Combats free radicals, reduces dark spots, and evens out your natural skin tone with continuous use.",
            imageUrl = "img_gel_vitamina_c"
        ),
        Product(
            id = "23",
            name = "Root Purifying Serum",
            price = 85.0,
            category = "Treatments",
            description = "Specialized spa-style treatment for the scalp. Soothes flaking, purifies follicles, and promotes healthy hair growth.",
            imageUrl = "img_tratamiento_cuero_cabelludo"
        ),
        Product(
            id = "24",
            name = "Nourishing Honey Mask",
            price = 55.0,
            category = "Treatments",
            description = "Formulated with raw honey and propolis extract. An intensive SOS treatment that deeply repairs, hydrates, and soothes stressed skin.",
            imageUrl = "img_mascarilla_miel"
        )
    )
    private val productsFlow = MutableStateFlow<List<Product>>(mockProducts.toList())

    override suspend fun getProducts(): List<Product> {
        delay(800)
        return productsFlow.value
    }

    override fun observeProducts(): Flow<List<Product>> {
        return productsFlow.asStateFlow()
    }

    override suspend fun addProduct(product: Product, imageBytes: ByteArray?): Boolean {
        delay(1000)

        val newProduct = product.copy(
            id = (mockProducts.size + 1).toString(),
            imageUrl = "img_placeholder"
        )

        mockProducts.add(newProduct)
        productsFlow.value = mockProducts.toList()

        return true
    }
}