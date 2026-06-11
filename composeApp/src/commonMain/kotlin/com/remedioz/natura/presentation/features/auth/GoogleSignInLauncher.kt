package com.remedioz.natura.presentation.features.auth

import androidx.compose.runtime.Composable

/**
 * Genera un lanzador multiplataforma para Google Sign-In.
 * Devuelve una lambda () -> Unit que dispara el flujo nativo de cada plataforma.
 */
@Composable
expect fun rememberGoogleSignInLauncher(
    onAuthSuccess: (String) -> Unit,
    onAuthError: (String) -> Unit
): () -> Unit