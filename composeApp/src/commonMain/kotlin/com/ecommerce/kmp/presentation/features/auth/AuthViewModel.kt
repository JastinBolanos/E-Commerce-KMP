package com.ecommerce.kmp.presentation.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ============================================================================
 * 🛡️ AUTHENTICATION VIEW MODEL & SESSION MANAGER
 * ============================================================================
 * * @description
 * This ViewModel manages the reactive state for the authentication flows
 * (both Admin and Customer). It strictly encapsulates mutable states using
 * the `MutableStateFlow` / `StateFlow` backing property pattern. It handles
 * simulated network latency, basic input validation, and session data
 * persistence in memory.
 * * 🔌 NOTE FOR BACKEND / IDENTITY TEAM:
 * This file currently utilizes mock delays (`delay(1500)`) and hardcoded
 * profile data to demonstrate the UI loading transitions.
 * To implement production Authentication:
 * 1. Inject an `AuthRepository` or `SessionManager` via the constructor.
 * 2. Replace the `delay()` in `loginAdmin` with a secure HTTP POST request
 * (e.g., Ktor/Retrofit call to `/api/v1/auth/login`), parse the response,
 * and store the resulting JWT token securely (e.g., EncryptedSharedPreferences).
 * 3. For `loginAsClient`, accept the native OAuth token provided by the UI,
 * send it to the backend for cryptographic validation, and map the real
 * JSON User payload to the `_userName`, `_userEmail`, and `_userPhotoUrl` states.
 * * @layer Presentation / Features / Auth
 * ============================================================================
 */

class AuthViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Datos simulados del usuario logueado
    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    private val _userPhotoUrl = MutableStateFlow<String?>(null)
    val userPhotoUrl: StateFlow<String?> = _userPhotoUrl.asStateFlow()

    /**
     * Inicia sesión simulada para el Administrador
     */
    fun loginAdmin(email: String, pass: String, onSuccess: () -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            _errorMessage.value = "Por favor, llena ambos campos."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            delay(1500)

            _isLoading.value = false

            // Dejamos pasar cualquier credencial,
            // pero podríamos poner un if(email == "admin@admin.com") si quisieras restringirlo.
            onSuccess()
        }
    }

    /**
     * Inicia sesión simulada como Cliente (Reemplaza al flujo de Google)
     */
    fun loginAsClient(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true

            // Simulamos la latencia de la API de Google
            delay(1500)

            // Inyectamos datos falsos para que el perfil de usuario se vea bonito
            _userName.value = "LAURA VERACRUZ"
            _userEmail.value = "laura.official@gmail.com"
            _userPhotoUrl.value = ""

            _loginSuccess.value = true
            _isLoading.value = false

            onSuccess()
        }
    }

    /**
     * Simula el cierre de sesión
     */
    fun logout() {
        _userName.value = null
        _userEmail.value = null
        _userPhotoUrl.value = null
        _loginSuccess.value = false
    }
}