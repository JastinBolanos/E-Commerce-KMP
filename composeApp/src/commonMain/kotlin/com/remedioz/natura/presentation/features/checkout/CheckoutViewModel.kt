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

            val order = Order(
                userId = userId,
                customerName = customerName,
                items = cartItems,
                totalAmount = total
            )

            val success = repository.createOrder(order, voucherBytes)

            _orderSuccess.value = success
            _isLoading.value = false
        }
    }

    fun resetState() {
        _orderSuccess.value = false
    }
}