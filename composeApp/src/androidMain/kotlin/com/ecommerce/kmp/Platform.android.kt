package com.ecommerce.kmp

import android.os.Build

/**
 * ============================================================================
 * 🤖 ANDROID PLATFORM IMPLEMENTATION (ACTUAL)
 * ============================================================================
 * * @description
 * This file provides the Android-specific implementation (`actual`) for the
 * multiplatform `Platform` contract. It retrieves native OS-level information,
 * specifically the Android Build Version SDK integer.
 * * 🔌 NOTE FOR BACKEND / DATA TEAM:
 * This native bridge is highly useful for telemetry, analytics, and dynamic
 * API routing. When connecting to a real Backend, you can inject this
 * platform information into your HTTP Interceptors (e.g., Ktor) to append
 * "Device-OS: Android XX" headers to every network request. This allows the
 * backend to track metrics or send OS-specific responses/configurations.
 * * @layer Platform / Android Interop
 * ============================================================================
 */

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()