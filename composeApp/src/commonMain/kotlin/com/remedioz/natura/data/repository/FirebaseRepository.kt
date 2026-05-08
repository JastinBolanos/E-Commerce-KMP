package com.remedioz.natura.data.repository

import com.remedioz.natura.domain.model.Product
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class FirebaseRepository {
    // Apuntamos a la base de datos de Firestore
    private val db = Firebase.firestore

    // Referencia directa a la "carpeta" (colección) de productos en la nube
    private val productsCollection = db.collection("products")

    /**
     * Función para OBTENER todos los productos de la tienda.
     * Usamos 'suspend' porque esta operación toma tiempo (debe viajar a internet y volver)
     * y no queremos que la pantalla se congele mientras carga.
     */
    suspend fun getProducts(): List<Product> {
        return try {
            // Descargamos los documentos y los convertimos automáticamente a nuestro modelo Product
            val snapshot = productsCollection.get()
            snapshot.documents.map { document ->
                document.data<Product>()
            }
        } catch (e: Exception) {
            println("Error al descargar productos: ${e.message}")
            emptyList() // Si falla, devolvemos una lista vacía para que no se caiga la app
        }
    }

    /**
     * Función para AGREGAR un nuevo producto (La usaremos en tu vista de Administrador)
     */
    suspend fun addProduct(product: Product): Boolean {
        return try {
            // EL CAMBIO ESTÁ AQUÍ: Le quitamos los () a document
            val newDocRef = productsCollection.document

            // 2. Le inyectamos ese ID al producto
            val productWithId = product.copy(id = newDocRef.id)

            // 3. Lo guardamos en la nube
            newDocRef.set(productWithId)

            true // Éxito
        } catch (e: Exception) {
            println("Error al guardar el producto: ${e.message}")
            false // Fallo
        }
    }
}