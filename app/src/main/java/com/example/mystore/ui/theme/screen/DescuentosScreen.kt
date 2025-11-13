package com.example.mystore.ui.theme.screen

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mystore.model.Descuento
import com.example.mystore.viewModel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescuentosScreen (navController: NavController, viewModel: HomeViewModel){
    val descuentos by viewModel.descuento.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Descuentos",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.Default.ArrowBack,
                            contentDescription = "volver")
                    }
                }
            )
        }
    ) {
        paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues )
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ){
            when{
                isLoading ->{
                    CircularProgressIndicator()
                }
                descuentos.isEmpty() ->{
                    Text("No hay descuentos disponibles")
                }
                else ->{
                    LazyColumn(
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(descuentos){ desc ->
                            DescuentoCard(desc)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun DescuentoCard(descuento: Descuento){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = descuento.nombre,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = descuento.descripcion,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = descuento.descuento,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = painterResource(id = descuento.imagen),
                contentDescription = descuento.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(120.dp)
                    .height(200.dp)
            )
        }
    }
}