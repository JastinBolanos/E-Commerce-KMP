package com.ecommerce.kmp.data.platform

/**
 * ============================================================================
 * ⏱️ MULTIPLATFORM TIME CONTRACT (EXPECT)
 * ============================================================================
 * * @description
 * This declaration utilizes Kotlin Multiplatform's `expect`/`actual` paradigm
 * to define a common contract for retrieving the current system timestamp.
 * It allows the shared domain and presentation layers (e.g., Order sorting,
 * Notification timestamps) to safely access time data without tightly coupling
 * the core logic to JVM or Apple native libraries.
 * * 🔌 NOTE FOR PLATFORM TEAM:
 * This `expect` function MUST have corresponding `actual` implementations
 * in the platform-specific source sets to compile:
 * - `androidMain`: Should map to `System.currentTimeMillis()`
 * - `iosMain`: Should map to `(NSDate().timeIntervalSince1970 * 1000).toLong()`
 * * @layer Data / Platform
 * ============================================================================
 */

expect fun getCurrentTimeMillis(): Long