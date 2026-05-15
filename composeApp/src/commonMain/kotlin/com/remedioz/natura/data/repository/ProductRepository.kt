package com.remedioz.natura.data.repository

import com.remedioz.natura.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>

    // Recibimos el producto y los bytes de la imagen
    suspend fun addProduct(product: Product, imageBytes: ByteArray?): Boolean
}