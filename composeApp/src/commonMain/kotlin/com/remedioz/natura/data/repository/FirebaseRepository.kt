package com.remedioz.natura.data.repository

import com.remedioz.natura.data.platform.toFirebaseData
import com.remedioz.natura.domain.model.Order
import com.remedioz.natura.domain.model.Product
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.storage
import kotlin.time.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirebaseRepository {
    // --- 1. INSTANCIAS DE FIREBASE ---
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val storage = Firebase.storage
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
            true
        } catch (e: Exception) {
            println("Error en Firebase Auth: ${e.message}")
            false
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
            val snapshot = productsCollection.get()
            snapshot.documents.map { document ->
                document.data<Product>()
            }
        } catch (e: Exception) {
            println("Error al descargar productos: ${e.message}")
            emptyList()
        }
    }

    /**
     * Función para AGREGAR un nuevo producto (La usaremos en tu vista de Administrador)
     */
    suspend fun addProduct(product: Product): Boolean {
        return try {
            val newDocRef = productsCollection.document
            val productWithId = product.copy(id = newDocRef.id)
            newDocRef.set(productWithId)
            true
        } catch (e: Exception) {
            println("Error al guardar el producto: ${e.message}")
            false
        }
    }

    /**
     * Sube la imagen del voucher a Firebase Storage y devuelve la URL pública.
     */
    private suspend fun uploadVoucherImage(imageBytes: ByteArray, orderId: String): String {
        return try {
            val storageRef = storage.reference.child("vouchers/$orderId.jpg")

            storageRef.putData(imageBytes.toFirebaseData())

            storageRef.getDownloadUrl()
        } catch (e: Exception) {
            println("Error subiendo voucher: ${e.message}")
            ""
        }
    }

    /**
     * Guarda la orden completa en la base de datos tras procesar el pago.
     * Esta es la función principal que llamará el ViewModel del Checkout.
     */
    suspend fun createOrder(order: Order, voucherBytes: ByteArray): Boolean {
        return try {
            val generatedOrderId = "ORD-${Clock.System.now().toEpochMilliseconds()}"

            val voucherUrl = uploadVoucherImage(voucherBytes, generatedOrderId)

            if (voucherUrl.isEmpty()) {
                println("Fallo al subir el voucher, cancelando creación de orden.")
                return false
            }

            val finalOrder = order.copy(
                id = generatedOrderId,
                voucherUrl = voucherUrl,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )

            db.collection("orders")
                .document(generatedOrderId)
                .set(finalOrder)

            true
        } catch (e: Exception) {
            println("Error al crear la orden: ${e.message}")
            false
        }
    }

    // --- NUEVAS FUNCIONES DE CONFIGURACIÓN DE PAGO ---
    private val settingsRef = db.collection("settings").document("payment")

    suspend fun updatePaymentSettings(imageBytes: ByteArray?, currentQrUrl: String, newPhoneNumber: String): Boolean {
        return try {
            var finalUrl = currentQrUrl

            if (imageBytes != null) {
                val storageRef = storage.reference.child("payment/qr_code.jpg")
                storageRef.putData(imageBytes.toFirebaseData())
                finalUrl = storageRef.getDownloadUrl()
            }

            settingsRef.set(mapOf(
                "qrUrl" to finalUrl,
                "phoneNumber" to newPhoneNumber
            ))
            true
        } catch (e: Exception) {
            println("Error subiendo configuración: ${e.message}")
            false
        }
    }

    fun observePaymentSettings(): Flow<PaymentSettings> {
        return settingsRef.snapshots.map { snapshot ->
            if (snapshot.exists) {
                PaymentSettings(
                    qrUrl = snapshot.get<String?>("qrUrl") ?: "",
                    phoneNumber = snapshot.get<String?>("phoneNumber") ?: "987 654 321"
                )
            } else {
                PaymentSettings()
            }
        }
    }
}

data class PaymentSettings(
    val qrUrl: String = "",
    val phoneNumber: String = "987 654 321"
)