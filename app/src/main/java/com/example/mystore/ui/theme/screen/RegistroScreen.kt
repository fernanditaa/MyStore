package com.example.mystore.ui.theme.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun ResistroScreen() {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarcontrasena by remember { mutableStateOf("") }

    //mensajes de error en campos vacios o mal escritos
    var nombreError by remember { mutableStateOf("") }
    var apellidoError by remember { mutableStateOf("") }
    var correoError by remember { mutableStateOf("") }
    var contrasenaError by remember { mutableStateOf("") }
    var confirmarcontrasenaError by remember { mutableStateOf("") }

    //con esto validamos que el correo sea valido
    val emailRegex = Regex("^[\\w.-]+@[\\w.-]+\\.(cl|com)$")
    val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d).{6,}$")

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = {nombre = it; nombreError= ""},
            isError = nombreError.isNotEmpty(),
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        if (nombreError.isNotEmpty()){
            Text(nombreError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }
        OutlinedTextField(
            value = apellido,
            onValueChange = {apellido= it; apellidoError= ""},
            isError = apellidoError.isNotEmpty(),
            label = {Text("Apellidos")},
            modifier = Modifier.fillMaxWidth()
        )
        if (apellidoError.isNotEmpty()){
            Text(apellidoError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }
        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                correoError = if (correo.isNotEmpty() && !emailRegex.matches(correo)) {
                "Su correo debe contener @ y terminar con .cl o .com"
            }else{
                ""
             }
        },
            label = {Text("e-mail")},
            modifier = Modifier.fillMaxWidth()
        )
        if (correoError.isNotEmpty()){
            Text(correoError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        OutlinedTextField(
            value = contrasena,
            onValueChange = {
                contrasena = it
                contrasenaError = when{
                    contrasena.isEmpty()-> "Debe ingresar una contraseña"
                    contrasena.length <8 -> "Debe tener a lo menos 8 carácteres"
                    !passwordRegex.matches(contrasena) -> "Debe contener letras y números"
                    else -> ""
                }
            },
            label = {Text("Contraseña")},
            modifier = Modifier.fillMaxWidth()
        )
        if (contrasenaError.isNotEmpty()){
            Text(contrasenaError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        OutlinedTextField(
            value = confirmarcontrasena,
            onValueChange = {
                confirmarcontrasena = it
                confirmarcontrasenaError = if(confirmarcontrasena != contrasena)
                    "las contraseñas deben coinsidir"
                else ""
            },
            label = {Text("Confirmar contraseña")},
            isError = confirmarcontrasenaError.isNotEmpty(),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        if (confirmarcontrasenaError.isNotEmpty()){
            Text(confirmarcontrasenaError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }
    }
}