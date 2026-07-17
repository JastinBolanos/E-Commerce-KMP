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

/**
 * ============================================================================
 * 🛠️ ADMIN VIEW MODEL & CATALOG MANAGER
 * ============================================================================
 * * @description
 * This ViewModel serves as the central command for the Admin product management
 * interface. It securely handles the merging of distinct data sources
 * (Standard Products and Promotional Kits) into a single reactive pipeline.
 * It utilizes `combine` and `stateIn` with `WhileSubscribed(5000)` to provide
 * highly performant, real-time filtering (Search & Category) without
 * overwhelming the main thread.
 * * 🔌 NOTE FOR BACKEND / DATA INTEGRATION TEAM:
 * This ViewModel is designed to handle both local and remote operations seamlessly.
 * When migrating to a real Backend environment:
 * 1. Image Uploads: The `saveProduct` function currently accepts `imageBytes: ByteArray?`.
 * Before calling the Repositories, these bytes should be uploaded to a CDN/Storage
 * (e.g., AWS S3, Cloudinary), and the resulting URL must be injected into the
 * `Product.imageUrl` field.
 * 2. Delete Operation: The `deleteProduct` method is currently a stub. It must be
 * connected to a `repository.deleteProduct(id)` suspending function that executes
 * a `DELETE` HTTP request to the backend.
 * 3. Error Handling: Consider implementing a robust `UiState` sealed interface or
 * event channel to show SnackBar notifications on network failures.
 * * @layer Presentation / Features / Admin
 * ============================================================================
 */

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

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val filteredProducts: StateFlow<List<Product>> = combine(
        _products, _searchQuery, _selectedCategory
    ) { products, query, category ->
        products.filter { product ->
            val matchesQuery = product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)

            val matchesCategory = if (category == "All") true else product.category.equals(
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
                println("Error loading products/kits: ${e.message}")
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
                    println("Error: The repository returned false upon saving.")
                }
            } catch (e: Exception) {
                println("Error saving product: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                println("Simulating deletion of the product with ID: $productId")
            } catch (e: Exception) {
                println("Error deleting product: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}