package com.remedioz.natura.presentation.features.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.auth.externals.GoogleAuthProvider
import dev.gitlive.firebase.auth.externals.signInWithPopup
import kotlinx.coroutines.await
import kotlinx.coroutines.launch

@Composable
actual fun rememberGoogleSignInLauncher(
    onAuthSuccess: (String) -> Unit,
    onAuthError: (String) -> Unit
): () -> Unit {
    val scope = rememberCoroutineScope()

    return {
        scope.launch {
            try {
                val provider = GoogleAuthProvider()
                signInWithPopup(Firebase.auth.js, provider).await()

                onAuthSuccess("WEB_SUCCESS")
            } catch (e: Exception) {
                onAuthError(e.message ?: "Error en Google Sign-In Web (JS)")
            }
        }
    }
}