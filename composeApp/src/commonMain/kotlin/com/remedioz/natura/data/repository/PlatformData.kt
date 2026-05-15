package com.remedioz.natura.data.repository

import dev.gitlive.firebase.storage.Data

// Le decimos a KMP: "Oye, en algún lugar te daré la receta para crear este Data"
expect fun createFirebaseData(bytes: ByteArray): Data