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

class HomeViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    // Origen de datos inmutable (Single Source of Truth).
    // Mantiene la data cruda aislada para evitar mutaciones accidentales desde la capa UI.
    private val _products = MutableStateFlow<List<Product>>(emptyList())

    // Estados elevados (State Hoisting) para mantener el patrón Unidirectional Data Flow (UDF).
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todos")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Transformación reactiva de múltiples flujos.
    // Se ejecuta fuera de la UI para no bloquear el Main Thread durante el filtrado de listas grandes.
    val filteredProducts: StateFlow<List<Product>> = combine(
        _products, _searchQuery, _selectedCategory
    ) { products, query, category ->
        products.filter { product ->
            val matchesQuery = product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)

            val matchesCategory = if (category == "Todos") true else product.category.equals(category, ignoreCase = true)

            matchesQuery && matchesCategory
        }
    }.stateIn(
        scope = viewModelScope,
        // WhileSubscribed(5000) previene que el flujo se destruya instantáneamente
        // durante rotaciones de pantalla, evitando recálculos innecesarios.
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadProducts()
    }

    // Hidratación asíncrona del estado delegada a una corrutina
    // para no bloquear la instanciación del ViewModel.
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