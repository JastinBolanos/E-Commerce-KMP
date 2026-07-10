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

/**
 * ============================================================================
 * ⚙️ ADMIN ORDERS VIEW MODEL & REACTIVE PIPELINE
 * ============================================================================
 * * @description
 * This ViewModel orchestrates the real-time order tracking and lifecycle
 * management for the Administrator dashboard. It establishes a direct,
 * hot `StateFlow` pipeline from the `OrderRepository` to the UI layer,
 * leveraging `stateIn` with `WhileSubscribed(5000)` to optimize background
 * resource consumption and prevent memory leaks.
 * * 🔌 NOTE FOR BACKEND / DATABASE TEAM:
 * The `orders` property is a continuous stream. In this architecture,
 * the UI does not "ask" for new orders; it simply reacts to emissions
 * from the repository. When migrating to a real Cloud Database (like Firestore
 * or Supabase Realtime), the `observeOrders()` implementation in the repository
 * must be connected to a live Snapshot Listener or WebSocket.
 * The `updateOrderStatus` method currently performs local mutation. In production,
 * this must execute an HTTP PATCH request and await a 200 OK response before
 * invoking the `onSuccess` callback to ensure data consistency.
 * * @layer Presentation / Features / Admin
 * ============================================================================
 */

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