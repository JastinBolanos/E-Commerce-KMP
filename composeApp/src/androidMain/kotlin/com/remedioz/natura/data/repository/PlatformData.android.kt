package com.remedioz.natura.data.repository

import dev.gitlive.firebase.storage.Data

// Aquí cumplimos la promesa: En Android, la clase Data SÍ acepta un ByteArray directo
actual fun createFirebaseData(bytes: ByteArray): Data {
    return Data(bytes)
}