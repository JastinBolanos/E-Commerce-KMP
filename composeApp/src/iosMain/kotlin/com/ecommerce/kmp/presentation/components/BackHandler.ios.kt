package com.ecommerce.kmp.presentation.components

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(isEnabled: Boolean, onBack: () -> Unit) {
    // En iOS no hay un botón físico de "Atrás" a nivel de sistema que podamos interceptar así.
    // La navegación hacia atrás en iOS ocurre por gestos táctiles (swipe)
    // que son manejados internamente por el sistema operativo o tu librería de navegación.
    // Por lo tanto, esta función se queda vacía (no-op) de forma intencional.
}