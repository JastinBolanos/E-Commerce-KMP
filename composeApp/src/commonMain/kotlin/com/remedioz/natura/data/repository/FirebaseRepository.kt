package com.remedioz.natura.data.repository

import com.remedioz.natura.domain.model.Product
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore

class FirebaseRepository {
    // --- 1. INSTANCIAS DE FIREBASE ---
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val productsCollection = db.collection("products")

    // --- 2. MÉTODOS DE AUTENTICACIÓN ---

    /**
     * Inicia sesión en Firebase usando las credenciales obtenidas de Google.
     * Recibe el idToken que Android/Web nos proporcionará tras el popup de Google.
     */
    suspend fun signInWithGoogle(idToken: String, accessToken: String? = null): Boolean {
        return try {
            val credential = GoogleAuthProvider.credential(idToken, accessToken)
            auth.signInWithCredential(credential)
            true // Login exitoso
        } catch (e: Exception) {
            println("Error en Firebase Auth: ${e.message}")
            false // Falló el login
        }
    }

    /**
     * Verifica si ya hay un usuario logueado al abrir la app.
     */
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

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