package com.remedioz.natura.data.platform

import dev.gitlive.firebase.storage.Data
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.ArrayBuffer

actual fun ByteArray.toFirebaseData(): Data {
    val webBuffer = this.asDynamic().buffer as ArrayBuffer
    val jsArray = Uint8Array(webBuffer)
    return Data(jsArray)
}