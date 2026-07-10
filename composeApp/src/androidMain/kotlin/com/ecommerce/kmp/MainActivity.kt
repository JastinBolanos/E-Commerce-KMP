package com.ecommerce.kmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

/**
 * ============================================================================
 * 🤖 ANDROID NATIVE ENTRY POINT (MAIN ACTIVITY)
 * ============================================================================
 * * @description
 * This is the primary native container and entry point for the Android target.
 * It hosts the Kotlin Multiplatform shared UI (`App()`) within a native
 * `ComponentActivity`. It implements modern Android UI guidelines, such as
 * `enableEdgeToEdge()` to draw the UI behind system bars for a premium look.
 * * 🔌 NOTE FOR BACKEND / NATIVE DEVOPS TEAM:
 * If the application requires native Android SDK initializations that cannot
 * be handled in the shared KMP module—such as Firebase App Check, Android-specific
 * Push Notification channels (FCM), Stripe/Payment SDK bootstrapping, or Crashlytics
 * initialization—those configurations MUST be placed inside the `onCreate` lifecycle
 * method, strictly *before* the `setContent` block.
 * * @layer Platform / Android Native Shell
 * ============================================================================
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}