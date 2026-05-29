package com.remedioz.natura.presentation.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remedioz.natura.domain.model.Product
import com.remedioz.natura.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ProductRepository
) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
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
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            try {
                _products.value = repository.getProducts()
            } catch (e: Exception) {
                println("Error en Home: ${e.message}")
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