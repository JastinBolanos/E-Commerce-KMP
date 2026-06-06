package com.remedioz.natura.presentation.components

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(isEnabled: Boolean = true, onBack: () -> Unit)