package com.remedioz.natura.presentation.features.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
actual fun rememberGoogleSignInLauncher(
    onAuthSuccess: (String) -> Unit,
    onAuthError: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current

    // 1. Configurar las opciones de Google Sign-In
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("369738371112-27q28eh90gn908aj8mtuf7rep2c704o7.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }

    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    // 2. Crear el lanzador de la actividad nativa de Android
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (idToken != null) {
                onAuthSuccess(idToken)
            } else {
                onAuthError("No se pudo obtener el ID Token de Google.")
            }
        } catch (e: Exception) {
            onAuthError(e.message ?: "Error desconocido en Google Sign-In Android")
        }
    }

    // Devuelve la acción que Android Studio ejecutará al hacer clic
    return {
        launcher.launch(googleSignInClient.signInIntent)
    }
}