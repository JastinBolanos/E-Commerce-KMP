package com.remedioz.natura.presentation.features.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remedioz.natura.data.repository.FirebaseRepository
import com.remedioz.natura.domain.model.CartItem
import com.remedioz.natura.domain.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import com.remedioz.natura.data.repository.PaymentSettings
import kotlin.time.Clock

/**
 * Orquestador del proceso de pagos.
 * Maneja la subida del voucher y la creación de la orden de manera asíncrona (UDF).
 */
class CheckoutViewModel(
    private val repository: FirebaseRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val paymentSettings: StateFlow<PaymentSettings> = repository.observePaymentSettings().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PaymentSettings()
    )

    private val _orderSuccess = MutableStateFlow(false)
    val orderSuccess: StateFlow<Boolean> = _orderSuccess.asStateFlow()

    fun processOrder(
        userId: String,
        customerName: String,
        cartItems: List<CartItem>,
        total: Double,
        voucherBytes: ByteArray
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            // 1. ARMAMOS EL PEDIDO
            val newOrder = Order(
                id = "",
                userId = userId,
                customerName = customerName,
                totalAmount = total,
                items = cartItems,
                status = "Pendiente",
                voucherUrl = "",
                timestamp = Clock.System.now().toEpochMilliseconds()
            )

            // 2. LO ENVIAMOS A FIREBASE
            val success = repository.createOrder(newOrder, voucherBytes)

            _isLoading.value = false

            if (success) {
                _orderSuccess.value = true
            } else {
                println("Error al procesar la orden en Firebase")
            }
        }
    }

    fun resetState() {
        _orderSuccess.value = false
    }
}