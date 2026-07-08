package com.ecommerce.kmp.presentation.features.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.kmp.domain.model.CartItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Mock de configuración de pagos para que la UI muestre los QR o números locales
data class PaymentSettings(
    val yapeNumber: String = "987 654 321",
    val yapeName: String = "Remedioz Natura S.A.C",
    val plinNumber: String = "987 654 321",
    val cci: String = "0011-0222-0200733364-11"
)

/**
 * Orquestador del proceso de pagos.
 * Maneja la subida del voucher y la creación de la orden de manera asíncrona (UDF).
 * (Versión Mockeada para Frontend Showcase)
 */
class CheckoutViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Exponemos los datos falsos para que la UI los pinte de inmediato sin ir a la red
    private val _paymentSettings = MutableStateFlow(PaymentSettings())
    val paymentSettings: StateFlow<PaymentSettings> = _paymentSettings.asStateFlow()

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

            // Simulamos el tiempo que tardaría subir la foto del Voucher y guardar en la BD
            delay(2500)

            _isLoading.value = false

            // Para el portafolio, siempre asumimos que la compra fue un éxito
            _orderSuccess.value = true
        }
    }

    fun resetState() {
        _orderSuccess.value = false
    }
}