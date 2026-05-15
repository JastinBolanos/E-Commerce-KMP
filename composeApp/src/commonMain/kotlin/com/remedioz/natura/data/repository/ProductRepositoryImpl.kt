package com.remedioz.natura.data.repository

import com.remedioz.natura.domain.model.Product
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.storage
import dev.gitlive.firebase.storage.Data

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

            // 1. Si hay imagen, la subimos a Storage primero
            if (imageBytes != null) {
                val imageRef = storage.reference.child("products/${newDocRef.id}.jpg")

                val firebaseData = createFirebaseData(imageBytes)
                imageRef.putData(firebaseData)

                finalImageUrl = imageRef.getDownloadUrl()
            }

            // 2. Le inyectamos el ID y la URL de la imagen al producto
            val productToSave = product.copy(
                id = newDocRef.id,
                imageUrl = finalImageUrl
            )

            // 3. Guardamos el texto en Firestore
            newDocRef.set(productToSave)
            true
        } catch (e: Exception) {
            println("Error al subir producto o imagen: ${e.message}")
            false
        }
    }
}