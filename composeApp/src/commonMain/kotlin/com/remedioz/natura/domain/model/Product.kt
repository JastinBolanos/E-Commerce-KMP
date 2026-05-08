package com.remedioz.natura.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String = "",
    val name: String = "",
    val price: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val extraImages: List<String> = emptyList(), // <-- Añade esto para la cuadrícula
    val category: String = "",
    val isFavorite: Boolean = false
)