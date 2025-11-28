package com.example.mystore.ui.theme.screen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mystore.viewModel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.io.File
import java.util.Locale
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.OutlinedButton


@Composable
fun RegistroUsuarioScreen(navController: NavController, homeViewModel: HomeViewModel) {

    //Modificar el boton de "Rgistrar" esta en la parte superior de nuevo XD
    //tambien hay que arreglar la validacon para usar la camara :(

    //Formulario
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

    //imagen, camara y galeria
    val context = LocalContext.current
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    val cameraUri = remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {selectedUri = it}
    }

    //archivo temporal para la camara
    fun createImageFile(context: Context): Uri {

        val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale("es")).format(Date())
        val file = File(context.cacheDir, "photo_$time.jpg")
        return FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider",
            file
        )
    }
    //Camara
    val takePicture = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success){
            cameraUri.value?.let { selectedUri = it }
        }
    }


    //con esto validamos que el correo sea valido
    val emailRegex = Regex("^[\\w.-]+@[\\w.-]+\\.(cl|com)$")
    val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d).{6,}$")

    val pasosCompletados = listOf(
        nombre.isNotBlank(),
        apellido.isNotBlank(),
        correo.isNotEmpty() && emailRegex.matches(correo),
        contrasena.isNotEmpty() && contrasena.length >= 8 && passwordRegex.matches(contrasena),
        confirmarcontrasena.isNotEmpty() && confirmarcontrasena == contrasena
    ).count { it }

    val targetProgress = pasosCompletados / 5f
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

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth()
        )

        //Seccion para foto de perfil
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
        ){
            if (selectedUri != null){
                Image(
                    painter = rememberAsyncImagePainter(selectedUri),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }else{
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Color.Gray
                )
            }
        }
        //Boton para elegir foto
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = {galleryLauncher.launch("")}) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Galeria")
            }
            OutlinedButton(onClick = {
                val uri = createImageFile(context)
                cameraUri.value = uri
                takePicture.launch(uri)
            }) {
                Icon(Icons.Default.PhotoCamera, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Cámara")
            }
        }

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
    }
        Button(
            onClick = {
                if(nombre.isNotEmpty()&&
                    apellido.isNotEmpty()&&
                    correo.isNotEmpty()&&
                    contrasena.isNotEmpty()&&
                    confirmarcontrasena.isNotEmpty()&&
                    nombreError.isEmpty()&&
                    apellidoError.isEmpty()&&
                    correoError.isEmpty() &&
                    contrasenaError.isEmpty()&&
                    confirmarcontrasenaError.isEmpty()
                    ){

                    homeViewModel.registrarUsuario(
                        nombre = nombre,
                        apellido = apellido,
                        correo = correo,
                        contrasena = contrasena,
                        foto = selectedUri
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
