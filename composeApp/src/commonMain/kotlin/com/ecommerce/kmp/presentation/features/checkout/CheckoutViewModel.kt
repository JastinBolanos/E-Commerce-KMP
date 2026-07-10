package com.ecommerce.kmp.presentation.features.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.kmp.data.platform.getCurrentTimeMillis
import com.ecommerce.kmp.domain.model.CartItem
import com.ecommerce.kmp.domain.model.Order
import com.ecommerce.kmp.domain.repository.OrderRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ============================================================================
 * 💸 CHECKOUT VIEW MODEL & TRANSACTION ORCHESTRATOR
 * ============================================================================
 * * @description
 * This ViewModel manages the business logic and asynchronous state during the
 * final payment verification process. It handles UI loading states, provides
 * mock payment settings (Bank details/QR), and constructs the final `Order`
 * object to be injected into the global `OrderRepository`.
 * * 🔌 NOTE FOR BACKEND / CLOUD ARCHITECTURE TEAM:
 * In this showcase version, the `processOrder` function simulates a network
 * delay and creates an in-memory order. The `voucherBytes` (the image uploaded
 * by the user) is currently bypassed with a "local_demo_voucher" string.
 * * To migrate to a real Cloud Backend (AWS, Firebase, Supabase):
 * 1. Convert `voucherBytes` into a File or multipart-form data.
 * 2. Upload the bytes to a Cloud Storage bucket (e.g., Firebase Storage / AWS S3).
 * 3. Await the secure download URL from the bucket.
 * 4. Perform an HTTP POST request (via Ktor/Retrofit) to create the Order in
 * the actual database, passing the real `voucherUrl`.
 * * @layer Presentation / Features / Checkout
 * ============================================================================
 */

data class PaymentSettings(
    val yapeNumber: String = "987 654 321",
    val yapeName: String = "Remedioz Natura S.A.C",
    val plinNumber: String = "987 654 321",
    val cci: String = "0011-0222-0200733364-11"
)

class CheckoutViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _paymentSettings = MutableStateFlow(PaymentSettings())
    val paymentSettings: StateFlow<PaymentSettings> = _paymentSettings.asStateFlow()

    private val _orderSuccess = MutableStateFlow(false)
    val orderSuccess: StateFlow<Boolean> = _orderSuccess.asStateFlow()

    fun processOrder(
        userId: String,
        customerName: String,
        cartItems: List<CartItem>,
        total: Double,
        voucherBytes: ByteArray?
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            // Simulamos el tiempo de subida a la nube
            delay(2000)

            // EL PEDIDO EN LA MEMORIA
            val newOrder = Order(
                id = "ORD-${(10000..99999).random()}",
                userId = userId.ifEmpty { "USER-NEW" },
                customerName = customerName.ifEmpty { "Cliente Nuevo" },
                items = cartItems,
                totalAmount = total,
                voucherUrl = "local_demo_voucher",
                status = "Pendiente",
                timestamp = getCurrentTimeMillis()
            )

            orderRepository.addOrder(newOrder)

            _isLoading.value = false
            _orderSuccess.value = true
        }
    }

    fun resetState() {
        _orderSuccess.value = false
    }
}