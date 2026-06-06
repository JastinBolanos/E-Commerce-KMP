package com.remedioz.natura.presentation.features.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remedioz.natura.data.repository.FirebaseRepository
import com.remedioz.natura.domain.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrdersViewModel(private val repository: FirebaseRepository) : ViewModel() {

    // Escucha todos los pedidos en vivo desde Firebase
    val orders: StateFlow<List<Order>> = repository.observeOrders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

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