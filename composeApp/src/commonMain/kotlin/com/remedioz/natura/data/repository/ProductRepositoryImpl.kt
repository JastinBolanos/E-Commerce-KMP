package com.remedioz.natura.data.repository

import com.remedioz.natura.data.platform.toFirebaseData
import com.remedioz.natura.domain.model.Product
import com.remedioz.natura.domain.repository.ProductRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.storage

class ProductRepositoryImpl : ProductRepository {
    private val db = Firebase.firestore
    private val storage = Firebase.storage
    private val productsCollection = db.collection("products")

    override suspend fun getProducts(): List<Product> {
        return try {
            val snapshot = productsCollection.get()
            snapshot.documents.map { document -> document.data<Product>() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addProduct(product: Product, imageBytes: ByteArray?): Boolean {
        return try {
            val newDocRef = productsCollection.document
            var finalImageUrl = ""

            if (imageBytes != null) {
                val imageRef = storage.reference.child("products/${newDocRef.id}.jpg")

                imageRef.putData(imageBytes.toFirebaseData())

                finalImageUrl = imageRef.getDownloadUrl()
            }

            val productToSave = product.copy(
                id = newDocRef.id,
                imageUrl = finalImageUrl
            )

            newDocRef.set(productToSave)
            true
        } catch (e: Exception) {
            println("Error al subir producto o imagen: ${e.message}")
            false
        }
    }
}