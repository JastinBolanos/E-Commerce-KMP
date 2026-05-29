package com.remedioz.natura.domain.repository

import com.remedioz.natura.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun addProduct(product: Product, imageBytes: ByteArray?): Boolean
}