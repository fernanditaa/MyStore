package com.example.mystore.ui.theme.screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Button
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.mystore.viewModel.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CompraScreen(navController: NavController, homeViewModel: HomeViewModel){

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var numerotarjeta by remember { mutableStateOf("") }
    var fechaVencimiento by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    //errores
    var nombreError by remember { mutableStateOf("") }
    var correoError by remember { mutableStateOf("") }
    var telefonoError by remember { mutableStateOf("") }
    var calleError by remember { mutableStateOf("") }
    var numeroError by remember { mutableStateOf("") }
    var ciudadError by remember { mutableStateOf("") }
    var comunaError by remember { mutableStateOf("") }
    var numerotarjetaError by remember { mutableStateOf("") }
    var fechaVencimientoError by remember { mutableStateOf("") }
    var cvvError by remember { mutableStateOf("") }

    //Regex para validar correo, telefono, tarjeta
    val emailRegex = Regex("^[\\w.-]+@[\\w.-]+\\.(cl|com)$")
    val phoneRegex = Regex("^\\d{9}$")
    val cardRegex = Regex("^\\d{16}$")
    val expiryRegex = Regex("^(0[1-9]|1[0-2])/\\d{2}$") //MM/AA
    val cvvRegex = Regex("^\\d{3}$")

    //estas son para los mensajes como "Compra finalizada"
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scaffoldState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val pasosCompletados = listOf(
        nombre.isNotBlank(),
        correo.isNotEmpty() && emailRegex.matches(correo),
        telefono.isNotEmpty(),
        calle.isNotEmpty(),
        numero.isNotEmpty(),
        ciudad.isNotEmpty(),
        comuna.isNotEmpty(),
        numerotarjeta.isNotEmpty(),
        fechaVencimiento.isNotEmpty(),
        cvv.isNotEmpty()

    ).count { it }

    var  ubicacion by remember { mutableStateOf("") }

    val targetProgress = pasosCompletados / 10f
    val animatedProgress by animateFloatAsState(targetValue = targetProgress, label = "registroProgress")

    // Manejo de permisos con ActivityResultLauncher
    val permisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            obtenerUbicacion(context) { loc ->
                loc?.let {
                    ubicacion = "Lat: ${it.latitude}, Lon: ${it.longitude}"
                } ?: run {
                    Toast.makeText(context, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Finalizar compra",
                        style =MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.Default.ArrowBack,
                            contentDescription = "volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = scaffoldState) }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(30.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        Text(
            text = "Datos de envio",
            style = MaterialTheme.typography.titleLarge

        )
            Spacer(modifier = Modifier.height(24.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

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

            OutlinedTextField(
            value = nombre,
            onValueChange = {nombre = it; nombreError = if (it.isBlank()) "Ingrese su nombre" else ""},
            isError = nombreError.isNotEmpty(),
            label = {Text(text = "Nombre completo")},
            modifier = Modifier.fillMaxWidth()
        )
            if (nombreError.isNotEmpty()){
                Text(nombreError, color = Color.Red)
            }

            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it; correoError = when{
                    (it.isBlank())->"Ingrese un correo"
                    !emailRegex.matches(it) -> "Correo invalido" else -> "" }
                                },
                isError = correoError.isNotEmpty() ,
                label = {Text(text = "Correo electrónico")},
                modifier = Modifier.fillMaxWidth()
            )
            if (correoError.isNotEmpty()) {
                Text(correoError, color = Color.Red)
            }

            OutlinedTextField(
                value = telefono,
                onValueChange = {telefono = it; telefonoError = when{
                    it.isBlank() -> "Ingerese su teléfono"
                    !phoneRegex.matches(it) -> "Telefono invalido" else -> ""
                }
                                },
                isError = telefonoError.isNotEmpty(),
                label = {Text(text = "Teléfono")},
                modifier = Modifier.fillMaxWidth()
            )
            if (telefonoError.isNotEmpty()){
                Text(telefonoError, color = Color.Red)
            }

        OutlinedTextField(
            value =calle,
            onValueChange = {calle = it; calleError = if (it.isBlank()) "ingrese su calle" else ""},
            isError = calleError.isNotEmpty(),
            label = {Text(text = "Calle")},
            modifier = Modifier.fillMaxWidth()
        )
            if (calleError.isNotEmpty()){
                Text(calleError, color = Color.Red)
            }
            OutlinedTextField(
                value =numero,
                onValueChange = {numero = it; numeroError = if (it.isBlank()) "Ingresa el número de la casa" else ""
                                },
                isError = numeroError.isNotEmpty(),
                label = {Text(text = "Número")},
                modifier = Modifier.fillMaxWidth()
            )
            if (numeroError.isNotEmpty()){
                Text(numeroError, color = Color.Red)
            }
            OutlinedTextField(
                value = ciudad,
                onValueChange = {ciudad = it; ciudadError = if (it.isBlank()) "Ingresa una ciudad" else ""},
                isError = ciudadError.isNotEmpty(),
                label = {Text(text = "Ciudad")},
                modifier = Modifier.fillMaxWidth()
            )
            if (ciudadError.isNotEmpty()){
                Text(ciudadError, color = Color.Red)
            }

            OutlinedTextField(
                value = comuna,
                onValueChange = {comuna = it; comunaError = if (it.isBlank()) "Ingresa una comuna" else ""},
                isError = comunaError.isNotEmpty(),
                label = {Text(text = "Comuna")},
                modifier = Modifier.fillMaxWidth()
            )
            if (comunaError.isNotEmpty()){
                Text(comunaError, color = Color.Red)
            }
                    // pedimos la ubicacion
                    OutlinedTextField(
                        value = ubicacion,
                        onValueChange = { ubicacion = it},
                        label = { Text("Ubicación")},
                        modifier = Modifier.fillMaxSize(),
                        enabled = false
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(onClick = {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED
                        ) {
                            // Permiso ya otorgado
                            obtenerUbicacion(context) { loc ->
                                loc?.let {
                                    ubicacion = "Lat: ${it.latitude}, Lon: ${it.longitude}"
                                } ?: run {
                                    Toast.makeText(context, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            // Pedimos permiso
                            permisoLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    }) {
                        Text("Obtener ubicación actual")
                    }

                    }
            }

        Spacer(modifier = Modifier.height(24.dp))



        Text(
            text = "Información de pago",
            style = MaterialTheme.typography.titleLarge
        )
            Spacer(modifier = Modifier.height(24.dp))

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

        OutlinedTextField(
            value = numerotarjeta,
            onValueChange = {numerotarjeta = it; numerotarjetaError = when{
                it.isBlank()->"Ingrese el numero de tarjeta"
                !cardRegex.matches(it) -> "Número de tarjeta invalido" else -> ""
            }
                            },
            isError = numerotarjetaError.isNotEmpty(),
            label = {Text(text = "Número de tarjeta")},
            leadingIcon = {Icon(Icons.Default.CreditCard, contentDescription = null)},
            keyboardOptions = KeyboardOptions(keyboardType= KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
            if (numerotarjetaError.isNotEmpty()){
                Text(numerotarjetaError, color = Color.Red)
            }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = fechaVencimiento,
                onValueChange = {fechaVencimiento = it; fechaVencimientoError = when{
                    it.isBlank()-> "Ingrese Fecha"
                    !expiryRegex.matches(it) -> "Fecha invalida" else -> ""
                }},
                isError = fechaVencimientoError.isNotEmpty(),
                label = {Text(text = "MM/AA")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            if (fechaVencimientoError.isNotEmpty()){
                Text(fechaVencimientoError, color = Color.Red)
            }
            OutlinedTextField(
                value = cvv,
                onValueChange = {cvv = it; cvvError = when{
                    it.isBlank()-> "El cvv es incorrecto"
                    !cvvRegex.matches(it)->"CVV invalido" else -> ""
                }},
                isError = cvvError.isNotEmpty(),
                label = {Text(text = "cvv")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            if (cvvError.isNotEmpty()){
                Text(cvvError, color = Color.Red)
            }
        }
         }
            }
            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (nombreError.isEmpty() &&
                        correoError.isEmpty() &&
                        telefonoError.isEmpty()&&
                        calleError.isEmpty()&&
                        numeroError.isEmpty()&&
                        ciudadError.isEmpty()&&
                        comunaError.isEmpty()&&
                        numerotarjetaError.isEmpty()&&
                        fechaVencimientoError.isEmpty()&&
                        cvvError.isEmpty()&&

                        nombre.isNotBlank() &&
                        correo.isNotBlank() &&
                        telefono.isNotBlank()&&
                        calle.isNotBlank()&&
                        numero.isNotBlank()&&
                        ciudad.isNotBlank()&&
                        comuna.isNotBlank()&&
                        numerotarjeta.isNotBlank()&&
                        fechaVencimiento.isNotBlank()&&
                        cvv.isNotBlank()
                        ){


                           Toast.makeText(context,"!Compra realizada con éxito¡", Toast.LENGTH_SHORT).show()
                            navController.navigate("home"){
                                popUpTo("home"){inclusive = true} }

                    }else{
                scope.launch{
                    scaffoldState.showSnackbar("Ingresa los datos")
                        }
                    }
                },
                enabled = animatedProgress >= 1f,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pagar")
            }

            Button(
                onClick = {
                    scope.launch { scaffoldState.showSnackbar(
                        message = "Compra cancelada",
                        duration = SnackbarDuration.Short
                    )
                    navController.navigate("home"){
                        popUpTo(route = "home") {inclusive = true  }
                    }
                    }
                },
            ) {
                Text("Cancelar")
            }

        }
    }
}
@SuppressLint("PermisoLocalizacion")
fun obtenerUbicacion(context: Context, onResult: (Location?)-> Unit){
    val fusedLocationProviderClient: FusedLocationProviderClient=
        LocationServices.getFusedLocationProviderClient(context)

    fusedLocationProviderClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            onResult(location)
        }
        .addOnFailureListener {
            onResult(null)
        }
}
