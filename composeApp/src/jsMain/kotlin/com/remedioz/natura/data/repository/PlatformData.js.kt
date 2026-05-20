package com.remedioz.natura.data.repository

import dev.gitlive.firebase.storage.Data
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.ArrayBuffer

actual fun createFirebaseData(bytes: ByteArray): Data {
    // 1. Extraemos el buffer y lo costeamos explícitamente a ArrayBuffer
    val webBuffer = bytes.asDynamic().buffer as ArrayBuffer

    // 2. Ahora el compilador sabe exactamente qué constructor elegir sin ambigüedades
    val jsArray = Uint8Array(webBuffer)

    // 3. Se lo entregamos a Firebase
    return Data(jsArray)
}