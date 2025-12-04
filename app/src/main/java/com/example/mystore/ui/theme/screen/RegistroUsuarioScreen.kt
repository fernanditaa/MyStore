package com.example.mystore.ui.theme.screen

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mystore.viewModel.HomeViewModel
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.foundation.shape.RoundedCornerShape


@Composable
fun RegistroUsuarioScreen(navController: NavController, homeViewModel: HomeViewModel) {

    //Modificar el boton de "Rgistrar" esta en la parte superior de nuevo XD
    //tambien hay que arreglar la validacon para usar la camara :(

    //Formulario
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarcontrasena by remember { mutableStateOf("") }

    //mensajes de error en campos vacios o mal escritos
    var nombreError by remember { mutableStateOf("") }
    var apellidoError by remember { mutableStateOf("") }
    var telefonoError by remember { mutableStateOf("") }
    var direccionError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var contrasenaError by remember { mutableStateOf("") }
    var confirmarcontrasenaError by remember { mutableStateOf("") }

    val context = LocalContext.current
    //con esto validamos que el correo sea valido
    val emailRegex = Regex("^[\\w.-]+@[\\w.-]+\\.(cl|com)$")
    val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d).{6,}$")

    val pasosCompletados = listOf(
        nombre.isNotBlank(),
        apellido.isNotBlank(),
        telefono.isNotBlank(),
        direccion.isNotBlank(),
        email.isNotEmpty() && emailRegex.matches(email),
        contrasena.isNotEmpty() && contrasena.length >= 8 && passwordRegex.matches(contrasena),
        confirmarcontrasena.isNotEmpty() && confirmarcontrasena == contrasena
    ).count { it }

    val targetProgress = pasosCompletados / 7f
    val animatedProgress by animateFloatAsState(targetValue = targetProgress, label = "registroProgress")

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro de usuario",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxSize(),
                shape =RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)

        ){
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxWidth()
                )

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
            value = telefono,
            onValueChange = {telefono= it; telefonoError= ""},
            isError = telefonoError.isNotEmpty(),
            label = {Text("Teléfono")},
            modifier = Modifier.fillMaxWidth()
        )
        if (telefonoError.isNotEmpty()){
            Text(telefonoError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }
        OutlinedTextField(
            value = direccion,
            onValueChange = {direccion= it; direccionError= ""},
            isError = direccionError.isNotEmpty(),
            label = {Text("Dirección")},
            modifier = Modifier.fillMaxWidth()
        )
        if (direccionError.isNotEmpty()){
            Text(direccionError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = if (email.isNotEmpty() && !emailRegex.matches(email)) {
                "Su correo debe contener @ y terminar con .cl o .com"
            }else{
                ""
             }
        },
            label = {Text("e-mail")},
            modifier = Modifier.fillMaxWidth()
        )
        if (emailError.isNotEmpty()){
            Text(emailError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        OutlinedTextField(
            value = contrasena,
            onValueChange = {
                contrasena = it
                contrasenaError = when{
                    it.isEmpty()-> "Debe ingresar una contraseña"
                    it.length <8 -> "Debe tener a lo menos 8 carácteres"
                    !passwordRegex.matches(contrasena) -> "Debe contener letras y números"
                    else -> ""
                }
            },
            label = {Text("Contraseña")},
            visualTransformation = PasswordVisualTransformation(),
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
        Button(
            onClick = {
                if(nombre.isNotEmpty()&&
                    apellido.isNotEmpty()&&
                    email.isNotEmpty()&&
                    telefono.isNotEmpty()&&
                    direccion.isNotEmpty()&&
                    contrasena.isNotEmpty()&&
                    confirmarcontrasena.isNotEmpty()&&
                    nombreError.isEmpty()&&
                    apellidoError.isEmpty()&&
                    emailError.isEmpty() &&
                    contrasenaError.isEmpty()&&
                    confirmarcontrasenaError.isEmpty()
                ){

                    homeViewModel.registrarUsuario(
                        nombre = nombre,
                        apellido = apellido,
                        telefono = telefono,
                        direccion = direccion,
                        email = email,
                        contrasena = contrasena
                    ){completado ->
                        if (completado){
                            Toast.makeText(
                                context,
                                "Registro exitoso",
                                Toast.LENGTH_SHORT).show()
                            navController.navigate("home"){
                                popUpTo ("registro"){inclusive = true  }
                            }
                        }else{
                            Toast.makeText(
                                context,
                                "Correo o contraseña incorrectos",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(context, "Asegurate que los campos sean correctos antes de continuar",
                        Toast.LENGTH_SHORT).show()
                }

            },
            enabled = animatedProgress >= 1f,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }
            }
        }
    }

}
