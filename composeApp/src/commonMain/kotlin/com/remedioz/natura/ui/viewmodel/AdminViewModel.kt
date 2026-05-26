package com.remedioz.natura.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remedioz.natura.data.repository.ProductRepository
import com.remedioz.natura.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Orquestador de estado y lógica de negocio para la consola de administración.
 * Aplica UDF (Unidirectional Data Flow) aislando las mutaciones de la base de datos
 * y exponiendo estados inmutables para garantizar consistencia y seguridad en la UI.
 */
class AdminViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todos")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Cálculo reactivo asíncrono.
    // Centraliza el filtrado aquí para evitar la sobrecarga del Main Thread (Recomposición de UI)
    // frente a catálogos masivos, manteniendo el ViewModel como Única Fuente de Verdad.
    val filteredProducts: StateFlow<List<Product>> = combine(
        _products, _searchQuery, _selectedCategory
    ) { products, query, category ->
        products.filter { product ->
            val matchesQuery = product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)

            val matchesCategory = if (category == "Todos") true else product.category.equals(category, ignoreCase = true)

            matchesQuery && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _products.value = repository.getProducts()
            } catch (e: Exception) {
                println("Error cargando productos: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateCategory(category: String) {
        _selectedCategory.value = category
    }

    fun saveProduct(product: Product, imageBytes: ByteArray?) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.addProduct(product, imageBytes)
                if (success) {
                    // Forzamos la resincronización explícita tras una mutación exitosa
                    // para garantizar que la memoria local refleje el estado real de la nube.
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
                // TODO: Implementar llamada al endpoint de eliminación cuando el repositorio lo soporte
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