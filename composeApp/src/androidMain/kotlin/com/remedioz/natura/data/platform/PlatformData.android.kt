package com.remedioz.natura.data.platform

import dev.gitlive.firebase.storage.Data

actual fun ByteArray.toFirebaseData(): Data {
    return Data(this)
}