package com.ecommerce.kmp.presentation.features.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.kmp.domain.model.Product
import com.ecommerce.kmp.domain.repository.KitRepository
import com.ecommerce.kmp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repository: ProductRepository,
    private val kitRepository: KitRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todos")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val filteredProducts: StateFlow<List<Product>> = combine(
        _products, _searchQuery, _selectedCategory
    ) { products, query, category ->
        products.filter { product ->
            val matchesQuery = product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)

            val matchesCategory = if (category == "Todos") true else product.category.equals(
                category,
                ignoreCase = true
            )

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
                val normalProducts = repository.getProducts()
                val kitProducts = kitRepository.getKits()

                _products.value = normalProducts + kitProducts
            } catch (e: Exception) {
                println("Error cargando productos/kits: ${e.message}")
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
                val isKit = product.category.equals("Kits", ignoreCase = true)

                val success = if (isKit) {
                    kitRepository.addKit(product, imageBytes)
                } else {
                    repository.addProduct(product, imageBytes)
                }

                if (success) {
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
                println("Simulando eliminación del producto con ID: $productId")
            } catch (e: Exception) {
                println("Error eliminando producto: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}