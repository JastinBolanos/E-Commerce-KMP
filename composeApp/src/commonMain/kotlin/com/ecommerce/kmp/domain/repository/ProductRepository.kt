package com.ecommerce.kmp.domain.repository

import com.ecommerce.kmp.domain.model.Product // 👈 Importación corregida
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    fun observeProducts(): Flow<List<Product>>
    suspend fun addProduct(product: Product, imageBytes: ByteArray?): Boolean
}