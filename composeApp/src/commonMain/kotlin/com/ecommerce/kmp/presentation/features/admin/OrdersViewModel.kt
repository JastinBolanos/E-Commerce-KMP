package com.ecommerce.kmp.presentation.features.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.kmp.domain.model.Order
import com.ecommerce.kmp.domain.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrdersViewModel(private val repository: OrderRepository) : ViewModel() {

    // Escucha todos los pedidos en vivo (ahora desde el Mock local)
    val orders: StateFlow<List<Order>> = repository.observeOrders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Función para Aprobar o Rechazar el voucher
    fun updateOrderStatus(orderId: String, newStatus: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = repository.updateOrderStatus(orderId, newStatus)
            _isLoading.value = false

            if (success) {
                onSuccess()
            }
        }
    }
}