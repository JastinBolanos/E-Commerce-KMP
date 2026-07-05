package com.ecommerce.kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform