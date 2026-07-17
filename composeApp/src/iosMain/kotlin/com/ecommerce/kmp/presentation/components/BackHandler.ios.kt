package com.ecommerce.kmp.presentation.components

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(isEnabled: Boolean, onBack: () -> Unit) {
    // In iOS, there is no physical system-level "Back" button that we can intercept like this.
    // Back navigation in iOS occurs via touch gestures (swipe)
    // which are handled internally by the operating system or your navigation library.
    // Therefore, this function is intentionally left empty (no-op).
}