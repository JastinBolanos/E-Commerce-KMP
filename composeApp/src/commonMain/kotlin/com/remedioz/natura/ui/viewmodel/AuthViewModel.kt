package com.remedioz.natura.ui.viewmodel

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

    // Estado para controlar si mostramos un indicador de carga en la UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado para avisarle a la UI que el login fue exitoso
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

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