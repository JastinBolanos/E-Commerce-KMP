package com.ecommerce.kmp.presentation.components

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(isEnabled: Boolean = true, onBack: () -> Unit)