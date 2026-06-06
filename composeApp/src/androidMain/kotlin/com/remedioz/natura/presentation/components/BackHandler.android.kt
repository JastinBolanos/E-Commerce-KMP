package com.remedioz.natura.presentation.components

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(isEnabled: Boolean, onBack: () -> Unit) {
    androidx.activity.compose.BackHandler(enabled = isEnabled, onBack = onBack)
}