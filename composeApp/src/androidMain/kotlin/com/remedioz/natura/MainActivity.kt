package com.remedioz.natura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

// --- NUEVO: Importamos el inicializador exclusivo de KMP ---
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- ¡AQUÍ ESTÁ LA MAGIA! ---
        // Despertamos a la librería KMP de Firebase pasándole el contexto
        Firebase.initialize(this)

        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}