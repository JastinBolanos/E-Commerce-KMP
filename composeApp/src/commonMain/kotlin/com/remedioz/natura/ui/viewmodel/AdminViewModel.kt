package com.remedioz.natura.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remedioz.natura.data.repository.ProductRepository
import com.remedioz.natura.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    // --- ESTADOS DE LA PANTALLA ---

    // 1. Estado para la lista de productos (los que vienen de Firebase)
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    // 2. Estado para mostrar un indicador de carga mientras sube la foto
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadProducts()
    }

    // --- FUNCIONES DE ACCIÓN ---

    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getProducts()
                _products.value = result
            } catch (e: Exception) {
                println("Error cargando productos: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveProduct(product: Product, imageBytes: ByteArray?) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Llamamos al repositorio para subir todo a Firebase
                val success = repository.addProduct(product, imageBytes)

                if (success) {
                    // Si se guardó con éxito, recargamos la lista para que aparezca en pantalla
                    loadProducts()
                } else {
                    println("Error: El repositorio devolvió false al guardar.")
                }
            } catch (e: Exception) {
                println("Error guardando producto: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // TODO: Añadiremos la función deleteProduct en el Repository luego
                println("Simulando eliminación del producto con ID: $productId")
                //repository.deleteProduct(productId)
                //loadProducts()
            } catch (e: Exception) {
                println("Error eliminando producto: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}