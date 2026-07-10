package com.ecommerce.kmp.domain.model

import kotlinx.serialization.Serializable

/**
 * ============================================================================
 * 🛍️ PRODUCT DOMAIN ENTITY
 * ============================================================================
 * * @description
 * Core catalog entity representing a sellable item or promotional kit.
 * Contains pricing, categorization, and image references. Ready for NoSQL
 * document mapping (Firestore/Mongo) or REST API JSON parsing.
 * * @layer Domain / Model
 * ============================================================================
 */

@Serializable
data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val imageUrl: String = "",
    val extraImages: List<String> = emptyList(),
    val category: String = "",
    val isFavorite: Boolean = false
)