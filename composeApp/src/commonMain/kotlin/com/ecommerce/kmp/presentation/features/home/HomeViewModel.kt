package com.ecommerce.kmp.presentation.features.home

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
 * 🧠 HOME VIEW MODEL & REACTIVE STATE ORCHESTRATOR
 * ============================================================================
 * * @description
 * This ViewModel serves as the reactive brain for the Customer Home Screen.
 * It strictly adheres to the Unidirectional Data Flow (UDF) architecture.
 * It utilizes Kotlin Coroutines and Flows (`combine`, `stateIn`) to seamlessly
 * derive the UI state (filtered lists) based on multiple asynchronous inputs
 * (search queries, category selection, and raw repository data).
 * * 🔌 NOTE FOR BACKEND / DATA INTEGRATION TEAM:
 * This ViewModel is fully optimized for Real-Time data streams (e.g., WebSockets,
 * Firebase Firestore snapshots, or Supabase real-time).
 * It continuously collects data from `repository.observeProducts()`. If the backend
 * pushes a change (like a price update or an out-of-stock flag), the Repositories
 * simply emit a new list through their Flow, and this ViewModel will automatically
 * trigger a UI recomposition without any additional imperative logic.
 * * @layer Presentation / Features / Home
 * ============================================================================
 */

class HomeViewModel(
    private val repository: ProductRepository,
    private val kitRepository: KitRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())

    private val _kits = MutableStateFlow<List<Product>>(emptyList())
    val kits: StateFlow<List<Product>> = _kits.asStateFlow()

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
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        observeDataInRealTime()
    }

    private fun observeDataInRealTime() {
        viewModelScope.launch {
            try {
                repository.observeProducts().collect { liveProducts ->
                    _products.value = liveProducts
                }
            } catch (e: Exception) {
                println("Error en Home escuchando productos: ${e.message}")
            }
        }

        viewModelScope.launch {
            try {
                kitRepository.observeKits().collect { liveKits ->
                    _kits.value = liveKits
                }
            } catch (e: Exception) {
                println("Error en Home escuchando kits: ${e.message}")
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateCategory(category: String) {
        _selectedCategory.value = category
    }
}