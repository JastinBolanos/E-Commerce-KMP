package com.ecommerce.kmp.data.repository

import com.ecommerce.kmp.domain.model.Product
import com.ecommerce.kmp.domain.repository.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockProductRepositoryImpl : ProductRepository {

    // Simula nuestra base de datos en la nube, pero vive en la memoria RAM
    private val mockProducts = mutableListOf(
        Product(
            id = "0",
            name = "Perfume Esencial de Lavanda",
            price = 40.0,
            category = "Belleza",
            description = "Extracto puro 100% natural para aliviar el estrés y mejorar el sueño nocturno.",
            imageUrl = "img_lavanda"
        ),
        Product(
            id = "1",
            name = "Aceite Esencial de Lavanda",
            price = 45.0,
            category = "Cuidados",
            description = "Extracto puro 100% natural para aliviar el estrés y mejorar el sueño nocturno.",
            imageUrl = "img_lavanda"
        ),
        Product(
            id = "2",
            name = "Infusión de Manzanilla y Miel",
            price = 15.5,
            category = "Tratamientos",
            description = "Mezcla natural para calmar el sistema digestivo después de comidas pesadas.",
            imageUrl = "img_manzanilla"
        ),
        Product(
            id = "3",
            name = "Crema de Aloe Vera Orgánica",
            price = 32.0,
            category = "Piel",
            description = "Hidratación profunda para pieles secas. Ayuda a regenerar marcas e irritaciones.",
            imageUrl = "img_aloe"
        ),

        // KIT
        Product(
            id = "4",
            name = "Kit Bienestar Integral",
            price = 85.0,
            category = "Kits",
            description = "El combo perfecto: Aceite de lavanda, infusión relajante y crema hidratante a un precio especial.",
            imageUrl = "img_placeholder"
        ),
        // KIT
        Product(
            id = "5",
            name = "Kit Resfrío y Defensas",
            price = 65.0,
            category = "Kits",
            description = "Infusiones de eucalipto, miel de abeja pura y extracto de propóleo para fortalecer tu sistema inmunológico frente a la humedad.",
            imageUrl = "img_placeholder"
        ),
        // KIT
        Product(
            id = "6",
            name = "Kit Spa Orgánico",
            price = 110.0,
            category = "Kits",
            description = "Sales de baño relajantes, mascarilla de arcilla andina y rodillo facial para tu rutina de cuidado personal en casa.",
            imageUrl = "img_placeholder"
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