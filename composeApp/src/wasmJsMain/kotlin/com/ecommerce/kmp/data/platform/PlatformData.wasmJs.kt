package com.ecommerce.kmp.data.platform

import kotlin.js.ExperimentalWasmJsInterop

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("() => Date.now()")
private external fun dateNow(): Double

actual fun getCurrentTimeMillis(): Long {
    return dateNow().toLong()
}
