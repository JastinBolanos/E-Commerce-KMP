package com.ecommerce.kmp.presentation.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    init {
        // En una app real, aquí revisaríamos si hay sesión.
        // Para el showcase, empezamos sin sesión para mostrar la pantalla de Login.
    }

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

            // Simulamos el tiempo de carga del servidor (1.5 segundos) para lucir la UI
            delay(1500)

            _isLoading.value = false

            // Para el portafolio, dejamos pasar cualquier credencial,
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
            _userName.value = "Usuario Portafolio"
            _userEmail.value = "reclutador@ejemplo.com"
            _userPhotoUrl.value = "" // Dejamos vacío para que Coil use el placeholder por defecto

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