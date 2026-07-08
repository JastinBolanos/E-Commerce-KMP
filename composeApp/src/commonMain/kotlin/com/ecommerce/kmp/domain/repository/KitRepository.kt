package com.ecommerce.kmp.domain.repository

import com.ecommerce.kmp.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface KitRepository {
    suspend fun getKits(): List<Product>

    fun observeKits(): Flow<List<Product>>

    suspend fun addKit(kit: Product, imageBytes: ByteArray? = null): Boolean
}