package com.ecommerce.kmp.domain.model

import kotlinx.serialization.Serializable

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