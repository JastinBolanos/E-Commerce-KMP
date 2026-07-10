package com.ecommerce.kmp.domain.repository

import com.ecommerce.kmp.domain.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * ============================================================================
 * 🎁 KIT REPOSITORY PORTAL
 * ============================================================================
 * * @description
 * Domain-level contract for managing Promotional Kits.
 * Exposes one-shot fetches (`getKits`), real-time reactive streams (`observeKits`),
 * and data creation mutations handling binary image payloads (`addKit`).
 * * @layer Domain / Repository
 * ============================================================================
 */

interface KitRepository {
    suspend fun getKits(): List<Product>

    fun observeKits(): Flow<List<Product>>

    suspend fun addKit(kit: Product, imageBytes: ByteArray? = null): Boolean
}