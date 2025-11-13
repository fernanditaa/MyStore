package com.example.mystore.ui.theme.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mystore.viewModel.HomeViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CompraScreen(navController: NavController, viewModel: HomeViewModel){

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

    val scrollState = rememberScrollState()

    //estas son para los mensajes como "Compra finalizada"
    val scaffoldState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Finalizazr compra",
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


        OutlinedTextField(
            value = nombre,
            onValueChange = {nombre = it},
            label = {Text(text = "Nombre completo")},
            modifier = Modifier.fillMaxWidth()
        )
            OutlinedTextField(
                value = correo,
                onValueChange = {correo = it},
                label = {Text(text = "Correo electrónico")},
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = telefono,
                onValueChange = {telefono = it},
                label = {Text(text = "Teléfono")},
                modifier = Modifier.fillMaxWidth()
            )

        OutlinedTextField(
            value =calle,
            onValueChange = {calle = it},
            label = {Text(text = "Calle")},
            modifier = Modifier.fillMaxWidth()
        )
            OutlinedTextField(
                value =numero,
                onValueChange = {numero = it},
                label = {Text(text = "Número")},
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = ciudad,
                onValueChange = {ciudad = it},
                label = {Text(text = "Ciudad")},
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = comuna,
                onValueChange = {comuna = it},
                label = {Text(text = "Comuna")},
                modifier = Modifier.fillMaxWidth()
            )


        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Información de pago",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = numerotarjeta,
            onValueChange = {numerotarjeta = it},
            label = {Text(text = "Número de tarjeta")},
            leadingIcon = {Icon(Icons.Default.CreditCard, contentDescription = null)},
            keyboardOptions = KeyboardOptions(keyboardType= KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = fechaVencimiento,
                onValueChange = {fechaVencimiento = it},
                label = {Text(text = "MM/AA")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = cvv,
                onValueChange = {cvv = it},
                label = {Text(text = "cvv")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }
            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    scope.launch { scaffoldState.showSnackbar(
                        message ="¡Compra realizada con éxito!",
                        duration = SnackbarDuration.Short
                    )
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                    }
                },
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
