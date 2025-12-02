package com.example.mystore.ui.theme.screen

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mystore.R
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.mystore.viewModel.HomeViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import android.Manifest
import android.graphics.BitmapFactory
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.asImageBitmap
import okhttp3.Callback


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen( navController: NavController, homeViewModel: HomeViewModel) {

    //para el usuario que inicia sesion
    val usuarioActual by homeViewModel.usuarioActual.collectAsState()
    val context = LocalContext.current
    //Foto guardada
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedImageRes by remember { mutableStateOf(R.drawable.imagen2) }

    //foto temporal
    var pendingImageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var isEditing by remember { mutableStateOf(false) }

    //creamos los datos para el usuario
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    LaunchedEffect(usuarioActual) {
        usuarioActual?.let{ u ->
            nombre = usuarioActual?.nombre ?: nombre
            correo = usuarioActual?.correo ?: correo
            telefono = usuarioActual?.telefono ?: telefono
            direccion = usuarioActual?.direccion ?: direccion

            usuarioActual?.foto?.let { path ->
                try {
                    val input= context.openFileInput(path)
                    val bmp = BitmapFactory.decodeStream(input)
                    input.close()
                    selectedImageBitmap = bmp
                }catch (e: Exception){
                    selectedImageBitmap = null
                }
            }
        }
    }
    val scrollState = rememberScrollState()

    //camara
    var onCameraPemissionGranted by remember { mutableStateOf<() -> Unit>({}) }

    val  cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        granted ->
        if (granted){
            onCameraPemissionGranted()
        }else{
            Toast.makeText(context,"Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            selectedImageBitmap = bitmap
            pendingImageBitmap = bitmap
        } else {
            Toast.makeText(context, "No se obtuvo imagen", Toast.LENGTH_SHORT).show()
        }
    }

    Spacer(modifier = Modifier.height(32.dp))


    Scaffold(
        topBar ={
            TopAppBar(
                title = {Text("Mi perfil")},
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.Filled.Person, contentDescription = "volver")
                    }
                }
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.Start
        ) {
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.BottomEnd
            ){
                if (selectedImageBitmap!= null){
                Image(
                    bitmap = selectedImageBitmap!!.asImageBitmap(),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
            }else{
                Image(
                    painter = painterResource(id = selectedImageRes),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, shape = CircleShape
                        )
                )
        }
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = "Cambiar imagen",
                    modifier = Modifier
                        .size(36.dp)
                        .padding(4.dp)
                        .clickable{
                            onCameraPemissionGranted = {
                                cameraLauncher.launch(null)
                            }
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                    tint = MaterialTheme.colorScheme.primary
                )
            }
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
            //campos para editar loos datos del usuario

            PerfilCampo(
                label = "Nombre",
                value = nombre,
                isEditing = isEditing,
                onChange = { nombre = it}
            )

            PerfilCampo(
                label = "Correo",
                value = correo,
                isEditing = isEditing,
                onChange = { correo = it}
            )

            PerfilCampo(
                label = "Telefono",
                value = telefono,
                isEditing = isEditing,
                onChange = { telefono = it}
            )

            PerfilCampo(
                label = "Dirección",
                value = direccion,
                isEditing = isEditing,
                onChange = { direccion = it}
            )
             }
                 }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isEditing){
                        homeViewModel.actualizarPerfil(
                            nombre = nombre,
                            correo = correo,
                            telefono = telefono,
                            direccion = direccion
                        ){
                            ok ->
                            if (!ok){
                                Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                            }
                        }
                        pendingImageBitmap?.let { bmp->
                            homeViewModel.actualizarFotoPerfil(bmp){ ok ->
                                if (ok){
                                    Toast.makeText(context,"Foto de perfil guardada", Toast.LENGTH_SHORT).show()
                                    selectedImageBitmap = bmp
                                    pendingImageBitmap = null
                                }else{
                                    Toast.makeText(context,"No se pudo guardar la foto de perfil",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    isEditing = !isEditing
                },

                modifier = Modifier.fillMaxWidth()
            )


            {
                Text(if (isEditing)"Guardar cambios" else "Editar perfil")
            }
            Spacer(modifier = Modifier.height(32.dp))


            Button(onClick = {navController.popBackStack()},
                modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Volver")
            }
        }
    }
}

@Composable

fun PerfilCampo(label: String, value: String, isEditing: Boolean, onChange: (String) -> Unit){
    Column(
        modifier = Modifier
            .padding(vertical = 6.dp)
    ) {
        Text(label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
        if (isEditing){
            OutlinedTextField(
                value = value,
                onValueChange = onChange,
                modifier = Modifier.fillMaxSize(),
                singleLine = true
            )
        } else{
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
