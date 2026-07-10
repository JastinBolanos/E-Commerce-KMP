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

    // Simula nuestra base de datos en la nube, pero vive en la memoria RAM
    private val mockProducts = mutableListOf(

        // --- CATEGORÍA 1: PIEL (Skin) ---
        Product(
            id = "1",
            name = "Sérum Iluminador Pink Peptide",
            price = 85.0,
            category = "Piel",
            description = "El secreto de la cosmética coreana. Péptidos concentrados que aportan un brillo de cristal y firmeza instantánea a tu rostro.",
            imageUrl = "img_serum_pink_peptide"
        ),
        Product(
            id = "2",
            name = "Crema Facial Nutritiva Dolkong",
            price = 68.0,
            category = "Piel",
            description = "Textura rica enriquecida con extractos de soya pura. Nutrición esencial para restaurar la barrera cutánea de las pieles secas.",
            imageUrl = "img_crema_dolkong"
        ),
        Product(
            id = "3",
            name = "Sérum Revitalizante de Rosas",
            price = 75.0,
            category = "Piel",
            description = "Fórmula concentrada con aceites botánicos de rosas. Hidrata profundamente y deja un acabado luminoso sin sensación grasa.",
            imageUrl = "img_tonico_purificante"
        ),
        Product(
            id = "4",
            name = "Crema de Noche Anti-Edad",
            price = 95.0,
            category = "Piel",
            description = "Tratamiento nocturno intensivo sin fragancia. Repara el tejido celular y reduce visiblemente las líneas de expresión mientras duermes.",
            imageUrl = "img_crema_noche"
        ),
        Product(
            id = "5",
            name = "Jabón Limpiador de Manzanilla",
            price = 25.0,
            category = "Piel",
            description = "Limpiador facial sólido ultra suave. Calma rojeces e irritaciones gracias a las propiedades desinflamantes de la manzanilla natural.",
            imageUrl = "img_jabon_manzanilla"
        ),
        Product(
            id = "6",
            name = "Jabón Purificante de Arcilla",
            price = 22.0,
            category = "Piel",
            description = "Ideal para pieles mixtas o con tendencia grasa. La arcilla verde desobstruye los poros y controla el exceso de sebo del rostro.",
            imageUrl = "img_jabon_arcilla"
        ),

        // --- CATEGORÍA 2: BELLEZA (Beauty) ---
        Product(
            id = "7",
            name = "Bálsamo Labial de Limón y Coco",
            price = 18.0,
            category = "Belleza",
            description = "Hidratación intensa con manteca de coco y extracto cítrico. Protege tus labios del sol mientras disfrutas de un aroma tropical y refrescante.",
            imageUrl = "img_balsamo_citrico"
        ),
        Product(
            id = "8",
            name = "Perfume Botánico Pasión Rubí",
            price = 145.0,
            category = "Belleza",
            description = "Una fragancia intensa y sofisticada. Combina notas de frutos rojos salvajes, ámbar y un toque de vainilla orgánica para ocasiones muy especiales.",
            imageUrl = "img_perfume_rojo"
        ),
        Product(
            id = "9",
            name = "Tinte Multi-usos Pétalo de Seda",
            price = 42.0,
            category = "Belleza",
            description = "Tinte natural para labios y mejillas. Su textura en crema ultraligera se difumina a la perfección aportando un color romántico y acabado aterciopelado.",
            imageUrl = "img_tinte_seda"
        ),
        Product(
            id = "10",
            name = "Eau de Parfum Brisa Oceánica",
            price = 130.0,
            category = "Belleza",
            description = "Frescura pura envasada. Notas de flor de loto, jazmín de agua y brisa marina que te envuelven en una sensación de limpieza y paz durante todo el día.",
            imageUrl = "img_perfume_azul"
        ),
        Product(
            id = "11",
            name = "Rubor Líquido Natural Glow",
            price = 55.0,
            category = "Belleza",
            description = "Un toque de color saludable que se funde con tu piel. Acabado jugoso, 100% vegano, altamente pigmentado y de larga duración sin obstruir los poros.",
            imageUrl = "img_rubor_liquido"
        ),
        Product(
            id = "12",
            name = "Bálsamo Reparador de Manzanilla",
            price = 20.0,
            category = "Belleza",
            description = "Cuidado experto para labios agrietados. Enriquecido con aceites esenciales de manzanilla y almendras dulces para reparar, calmar y proteger del frío extremo.",
            imageUrl = "img_balsamo_herbal"
        ),

        // --- CATEGORÍA 3: CUIDADOS (Care) ---
        Product(
            id = "13",
            name = "Aceite Esencial de Argán Puro",
            price = 58.0,
            category = "Cuidados",
            description = "El \"oro líquido\" para tu rutina. Un aceite puro multiusos ideal para hidratar profundamente la piel y revitalizar las puntas secas del cabello.",
            imageUrl = "img_aceite_dorado"
        ),
        Product(
            id = "14",
            name = "Mantequilla Corporal Sweet Vanilla",
            price = 45.0,
            category = "Cuidados",
            description = "Un postre para tu piel. Bálsamo corporal ultra nutritivo que se derrite al contacto, dejando una hidratación profunda y un aroma irresistible.",
            imageUrl = "img_mantequilla_caramelo"
        ),
        Product(
            id = "15",
            name = "Aceite Curativo Botánico",
            price = 62.0,
            category = "Cuidados",
            description = "Mezcla orgánica enriquecida con macadamia. Ayuda a reparar cicatrices, suavizar estrías y mejorar notablemente la elasticidad natural de tu piel.",
            imageUrl = "img_aceite_macadamia"
        ),
        Product(
            id = "16",
            name = "Shampoo Sólido Revitalizante",
            price = 35.0,
            category = "Cuidados",
            description = "Alternativa ecológica y sin envases. Limpia el cuero cabelludo suavemente con ingredientes botánicos, generando una espuma rica y purificante.",
            imageUrl = "img_shampoo_solido"
        ),
        Product(
            id = "17",
            name = "Exfoliante Corporal Luminoso",
            price = 52.0,
            category = "Cuidados",
            description = "Renueva la textura de tu cuerpo en la ducha. Cristales finos y aceites esenciales que eliminan células muertas y dejan tu piel sedosa y perfumada.",
            imageUrl = "img_exfoliante_corporal"
        ),
        Product(
            id = "18",
            name = "Jabón Artesanal de Avena",
            price = 18.0,
            category = "Cuidados",
            description = "Elaborado a mano con avena entera y extracto de manzanilla. Exfolia suavemente y calma las pieles más sensibles, ideal para uso diario.",
            imageUrl = "img_jabon_avena"
        ),

        // --- CATEGORÍA 4: TRATAMIENTOS (Treatments) ---
        Product(
            id = "19",
            name = "Esencia Facial Equilibrante",
            price = 65.0,
            category = "Tratamientos",
            description = "Tratamiento en esencia de textura lechosa. Prepara la piel tras la limpieza, equilibrando el pH y maximizando la absorción de tus sérums posteriores.",
            imageUrl = "img_esencia_lechosa"
        ),
        Product(
            id = "20",
            name = "Mascarilla Facial Iluminadora",
            price = 24.0,
            category = "Tratamientos",
            description = "Mascarilla de velo enriquecida con vitamina C y extractos de pomelo. Un tratamiento flash de 15 minutos para revivir y energizar pieles apagadas.",
            imageUrl = "img_mascarilla_citrica"
        ),
        Product(
            id = "21",
            name = "Tratamiento Reparador Capilar",
            price = 110.0,
            category = "Tratamientos",
            description = "Sistema intensivo de reconstrucción para cabello dañado. Repara los enlaces capilares desde el interior, devolviendo fuerza, suavidad y brillo.",
            imageUrl = "img_tratamiento_capilar"
        ),
        Product(
            id = "22",
            name = "Gel Tratamiento Vitamina C",
            price = 78.0,
            category = "Tratamientos",
            description = "Concentrado antioxidante en textura gel-sérum. Combate los radicales libres, atenúa manchas y unifica el tono natural de tu rostro con uso continuo.",
            imageUrl = "img_gel_vitamina_c"
        ),
        Product(
            id = "23",
            name = "Sérum Purificante de Raíz",
            price = 85.0,
            category = "Tratamientos",
            description = "Tratamiento especializado estilo spa para el cuero cabelludo. Alivia la descamación, purifica los folículos y promueve un crecimiento capilar sano.",
            imageUrl = "img_tratamiento_cuero_cabelludo"
        ),
        Product(
            id = "24",
            name = "Mascarilla Nutritiva de Miel",
            price = 55.0,
            category = "Tratamientos",
            description = "Formulada con miel cruda y extracto de propóleo. Un tratamiento SOS intensivo que repara, hidrata a profundidad y calma la piel estresada.",
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