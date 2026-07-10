package com.ecommerce.kmp.domain.repository

import com.ecommerce.kmp.domain.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * ============================================================================
 * 🛍️ PRODUCT REPOSITORY PORTAL
 * ============================================================================
 * * @description
 * Domain-level contract for the primary Store Catalog.
 * Standardizes CRUD operations and reactive data retrieval for standard
 * products, completely decoupling the UI from the underlying database implementation.
 * * @layer Domain / Repository
 * ============================================================================
 */

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    fun observeProducts(): Flow<List<Product>>
    suspend fun addProduct(product: Product, imageBytes: ByteArray?): Boolean
}