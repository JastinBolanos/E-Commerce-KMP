package com.ecommerce.kmp.presentation.components

import androidx.compose.runtime.Composable

/**
 * ============================================================================
 * 🔙 HARDWARE & GESTURE BACK NAVIGATION INTERCEPTOR
 * ============================================================================
 * * @description
 * This component provides a multiplatform contract to intercept system back-press
 * events (hardware buttons or native back gestures) inside Composable screens.
 * * - `Android`: Intercepts the native hardware back button or systemic back gesture
 * using Jetpack Compose's native `androidx.activity.compose.BackHandler`.
 * - `iOS`: Operates as an intentional No-Op (empty function). iOS handles back
 * navigation predominantly via edge-swipe touch gestures managed at
 * the root UINavigationController layer or navigation wrapper.
 * * 🔌 NOTE FOR BACKEND / MIGRATION TEAM:
 * This component is an OS-level layout interceptor. If you choose to migrate
 * the application's routing framework from this custom state-driven approach to
 * a robust third-party multiplatform library (such as Decompose, Voyage, or
 * Compose Multiplatform Navigation), this abstraction can be safely deprecated
 * or adapted, as those libraries handle platform-specific back events internally
 * through their own lifecycle controllers.
 * * @layer Presentation / Components
 * ============================================================================
 */

@Composable
expect fun BackHandler(isEnabled: Boolean = true, onBack: () -> Unit)