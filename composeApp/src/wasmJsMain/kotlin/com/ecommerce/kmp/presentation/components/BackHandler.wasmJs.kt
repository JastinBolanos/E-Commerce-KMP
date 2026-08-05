package com.ecommerce.kmp.presentation.components

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(isEnabled: Boolean, onBack: () -> Unit) {
    // In Web (Wasm), we don't have a hardware back button.
    // Intercepting browser back button requires different handling (e.g. window.onpopstate),
    // but for now we follow the no-op pattern like in iOS.
}
