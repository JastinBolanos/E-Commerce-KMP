package com.ecommerce.kmp

/**
 * ============================================================================
 * KMP PLATFORM ABSTRACTION & NATIVE BRIDGES
 * ============================================================================
 * * @description
 * This module leverages Kotlin Multiplatform's `expect`/`actual` mechanism.
 * It serves as a structural bridge connecting the shared core logic (commonMain)
 * with native, platform-specific APIs (androidMain, iosMain).
 * * - `expect`: Defines the contract or interface in the shared module.
 * - `actual`: Provides the native, low-level implementation for each OS.
 * * NOTE FOR BACKEND / NATIVE INTEGRATION TEAM:
 * If the application needs to access specific device hardware or OS-level
 * features (e.g., GPS, local KeyStore, push notification tokens, or native
 * cryptographic algorithms) that differ between iOS and Android, this is the
 * architectural pattern to follow. Define the `expect` contract here and
 * write the native implementations in their respective targets.
 * * @layer Platform / Native Interop
 * ============================================================================
 */

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform