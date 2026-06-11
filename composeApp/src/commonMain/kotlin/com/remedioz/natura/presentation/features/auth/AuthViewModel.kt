package com.remedioz.natura.presentation.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remedioz.natura.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: FirebaseRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /**
     * 🟢 Inicia sesión con Correo y Contraseña (Exclusivo para el Administrador)
     */
    fun loginAdmin(email: String, pass: String, onSuccess: () -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            _errorMessage.value = "Por favor, llena ambos campos."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val success = repository.signInWithEmailAndPassword(email, pass)

            _isLoading.value = false

            if (success) {
                onSuccess()
            } else {
                _errorMessage.value = "Credenciales incorrectas o usuario no existe."
            }
        }
    }

    /**
     * Esta función la llamaremos cuando obtengamos el Token de Google.
     */
    fun loginWithGoogleToken(idToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = repository.signInWithGoogle(idToken = idToken)
            _loginSuccess.value = success
            _isLoading.value = false
        }
    }
}