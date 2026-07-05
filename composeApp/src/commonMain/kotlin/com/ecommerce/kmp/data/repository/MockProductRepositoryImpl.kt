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
            id = "1",
            name = "Aceite Esencial de Lavanda",
            price = 45.0,
            category = "Relajación",
            description = "Extracto puro 100% natural para aliviar el estrés y mejorar el sueño nocturno.",
            imageUrl = "img_lavanda" // Nombre del recurso local que pondremos después
        ),
        Product(
            id = "2",
            name = "Infusión de Manzanilla y Miel",
            price = 15.5,
            category = "Digestión",
            description = "Mezcla natural para calmar el sistema digestivo después de comidas pesadas.",
            imageUrl = "img_manzanilla"
        ),
        Product(
            id = "3",
            name = "Crema de Aloe Vera Orgánica",
            price = 32.0,
            category = "Cuidado Piel",
            description = "Hidratación profunda para pieles secas. Ayuda a regenerar marcas e irritaciones.",
            imageUrl = "img_aloe"
        )
    )

    // Un "Flow" reactivo: si el admin añade un producto, la Home se entera al instante
    private val productsFlow = MutableStateFlow<List<Product>>(mockProducts.toList())

    override suspend fun getProducts(): List<Product> {
        delay(800) // Simulamos la latencia de internet para lucir el Loading State de la UI
        return productsFlow.value
    }

    override fun observeProducts(): Flow<List<Product>> {
        return productsFlow.asStateFlow()
    }

    override suspend fun addProduct(product: Product, imageBytes: ByteArray?): Boolean {
        delay(1000) // Simulamos la subida a un servidor

        val newProduct = product.copy(
            id = (mockProducts.size + 1).toString(), // Generamos un ID automático
            imageUrl = "img_placeholder" // En el mock, los productos nuevos usan una foto por defecto
        )

        mockProducts.add(newProduct)
        productsFlow.value = mockProducts.toList() // Avisamos a la UI que hay datos nuevos

        return true // Simulamos que se subió con éxito
    }
}

