package com.remedioz.natura.data.repository

import dev.gitlive.firebase.storage.Data

actual fun createFirebaseData(bytes: ByteArray): Data {
    return Data(bytes)
}