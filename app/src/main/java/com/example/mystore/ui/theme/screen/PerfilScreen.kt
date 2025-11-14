package com.example.mystore.ui.theme.screen

import android.net.Uri
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen( navController: NavController) {
    var selectedImage by remember { mutableStateOf(R.drawable.imagen2) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    //creamos los datos para el usuario

    var nombre by remember { mutableStateOf("Kat Hub") }
    var correo by remember { mutableStateOf("kathub@kathub.cl") }
    var telefono by remember { mutableStateOf("+56 9 4512 4512") }
    var direccion by remember { mutableStateOf("Calle 1, 123, Maipu, Region Metropolitana") }

    //Creamos album de imagenes locales
    val albumImages = listOf(
        R.drawable.imagen3,
        R.drawable.imagen5,
        R.drawable.imagen6,
        R.drawable.imagen7,
    )
    Spacer(modifier = Modifier.height(32.dp))


    Scaffold(
        topBar ={
            TopAppBar(
                title = {Text("Mi perfil")},
                navigationIcon = {
                        Icon(Icons.Filled.Person, contentDescription = "volver")

                }
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.BottomEnd){
                Image(
                    painter = painterResource(id = selectedImage),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(2.dp,
                            MaterialTheme.colorScheme.primary,
                            shape = CircleShape)
                )
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = "Cambiar imagen",
                    modifier = Modifier
                        .size(36.dp)
                        .padding(4.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            //campos para editar loos datos del usuario

            OutlinedTextField(
                value = nombre,
                onValueChange = {nombre = it},
                label = {Text("Nombre")},
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = correo,
                onValueChange = {correo = it},
                label = {Text("Correo")},
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = {telefono = it},
                label = {Text("Tel+efono")},
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it},
                label = {Text("Dirección")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {isEditing = !isEditing},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing)"Guardar cambios" else "Editar perfil")
            }
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Galería de imágenes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(albumImages.size){ index ->
                    val imageRes = albumImages[index]
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "Imagen de galería",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .border(
                                width = if (imageRes == selectedImage) 2.dp else 0.dp,
                                color = if (imageRes == selectedImage)
                                    MaterialTheme.colorScheme.primary
                                            else
                                    MaterialTheme.colorScheme.background,
                                shape = CircleShape
                            )
                            .clickable{selectedImage = imageRes}
                    )
                }
            }

            Button(onClick = {navController.popBackStack()}) {
                Text("Volver")
            }
        }
    }
}

@Composable

fun PerfilCampo(label: String, value: String, isEditing: Boolean, onChange: (String) -> Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
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