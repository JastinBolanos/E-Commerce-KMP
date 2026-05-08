package com.remedioz.natura

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform