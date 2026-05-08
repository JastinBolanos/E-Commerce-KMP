package com.remedioz.natura.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // <-- Importante para el estado
import androidx.compose.runtime.mutableStateOf // <-- Importante para el estado
import androidx.compose.runtime.remember // <-- Importante para el estado
import androidx.compose.runtime.setValue // <-- Importante para el estado
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchInput(modifier: Modifier = Modifier) {
    // Aquí creamos la memoria del buscador. Empieza vacío ("")
    var searchText by remember { mutableStateOf("") }

    OutlinedTextField(
        // Conectamos el valor del texto a nuestra variable
        value = searchText,

        // Cada vez que tecleas algo, actualizamos la variable
        onValueChange = { nuevoTexto ->
            searchText = nuevoTexto
        },

        placeholder = { Text("Buscar Productos", color = Color.Gray) },
        trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.Gray) },
        shape = RoundedCornerShape(50),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = Color.Black,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            cursorColor = Color.Black // Agregamos el cursor negro para que se vea bien
        )
    )
}