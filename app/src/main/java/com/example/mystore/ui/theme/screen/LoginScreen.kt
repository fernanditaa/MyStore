package com.example.mystore.ui.theme.screen

import android.widget.Toast
import com.example.mystore.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.example.mystore.viewModel.HomeViewModel
import kotlinx.coroutines.delay
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material3.OutlinedTextFieldDefaults



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController,homeViewModel: HomeViewModel){


    var email by remember { mutableStateOf("") }
    var emailError by remember {mutableStateOf("")  }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }

    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }



            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp),
                contentAlignment = Alignment.Center
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(32.dp)
                ){
                    Image(
                        painter = painterResource(id = R.drawable.imagen2),
                        contentDescription = "Logo KatHub",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    )
                    Text(
                        "Bienvenido a KatHub",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        "Inicio de sesión",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = {email = it
                                        emailError = ""},
                        label = {Text("Correo electrónico")},
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF333333),
                            unfocusedBorderColor = Color(0xFF555555)
                        )
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it},
                        label = {Text("Contraseña")},
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF333333),
                            unfocusedBorderColor = Color(0xFF555555)
                        )

                    )

                    Button(
                        onClick= {
                            if (email.isBlank()|| password.isBlank()){
                                Toast.makeText(context,"Ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show()
                            }else{
                                homeViewModel.validarLogin(
                                    correo = email,
                                    contrasena = password
                                ){
                                    valido->
                                    if (valido){
                                        Toast.makeText(context, "Inisiando sesión...", Toast.LENGTH_SHORT).show()
                                        navController.navigate("home"){
                                            popUpTo ("login"){ inclusive = true }
                                        }
                                    }else{
                                        loginError = "Correo o contraseña no coinciden"
                                        Toast.makeText(context, "Correo o contraseña no coinciden",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    ){
                        Text("Iniciar sesión")
                    }
                    if(loginError.isNotEmpty()){
                        Text(
                            text = loginError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    if (isLoading){
                        LaunchedEffect(Unit) {
                            delay(2000L)
                            navController.navigate("home") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        }
                    }
                    TextButton(
                        onClick = {navController.navigate("registro")}
                    ) {
                        Text("¿No tienes cuenta? Regístrate")
                    }

                }
            }


}

