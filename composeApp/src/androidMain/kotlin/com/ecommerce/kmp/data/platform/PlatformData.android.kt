package com.ecommerce.kmp.data.platform

actual fun getCurrentTimeMillis(): Long {
    return System.currentTimeMillis()
}