package com.example.mystore.ui.theme.screen

import android.service.quickaccesswallet.WalletCard
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystore.model.Producto
import com.example.mystore.viewModel.HomeViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController


@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel(), navController: NavController){

    val producto by viewModel.producto.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val cartCount by viewModel.carItemCount.collectAsState()

    Scaffold(
        topBar = { TopBar(cartCount = cartCount, navController = navController)}
    ){ paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()){
            if (isLoading){
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }else{
                LazyColumn(contentPadding = PaddingValues(16.dp)){
                    items(producto){ producto->
                        ProductCard(producto = producto, onAddToCart = {viewModel.agregarCarrito(producto) })
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(cartCount: Int, navController: NavController){
    TopAppBar(
        title = {Text("KatHub")},
        navigationIcon = {
            IconButton(onClick = {navController.navigate("Login")}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Cerrar sesión"
                )
            }
        },
        actions = {
            IconButton(onClick = {navController.navigate("Carrito")}) {
                BadgedBox(
                    badge = {
                        if (cartCount > 0){
                            Badge { Text(cartCount.toString()) }
                        }
                    }
                ) {
                    Icon(Icons.Filled.ShoppingCart,contentDescription = "Carrito")
                }
            }
        }
    )
}
@Composable
fun ProductCard(producto: Producto, onAddToCart: (Producto)-> Unit){
    Card (
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Column(modifier = Modifier.padding(16.dp)
        ){
            Image(
                painter = painterResource(id = producto.imagen),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(bottom = 8.dp),
                contentScale = ContentScale.Crop
            )
            Text(producto.nombre,
                style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.height(4.dp))

            Text("${producto.precio}",
                style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            Spacer(Modifier.height(8.dp))

            Text(producto.descripcion,
                style = MaterialTheme.typography.bodyMedium)

            Button(
                onClick = {onAddToCart(producto)},
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("Añadir al carrito")
            }
        }
    }
}

