package com.remedioz.natura.domain.repository

import com.remedioz.natura.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    fun observeProducts(): Flow<List<Product>>
    suspend fun addProduct(product: Product, imageBytes: ByteArray?): Boolean
}