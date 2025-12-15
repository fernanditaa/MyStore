package com.example.mystore.ui.theme.screen


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mystore.viewModel.HomeViewModel
import com.example.mystore.model.CarItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun CartScreen(navController: NavController, viewModel: HomeViewModel, ) {

    val cartItems by viewModel.carItem.collectAsState()

    val total = remember (cartItems){
        cartItems.sumOf { it.producto.precio * it.quantity }
    }

    Scaffold(
        topBar = { CartTopBar(navController = navController) },
        bottomBar = { CartBottomBar(total = total, onCheckout = { navController.navigate("finalizar compra") }) }
    ) { paddingValues ->

        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Su carrito está vacío.", style = MaterialTheme.typography.titleLarge)
            }
        } else {
            // 2. Mostrar la lista de ítems
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartItems){ item ->
                    CartItemRow(item = item,
                        onDelete= {producto ->
                            viewModel.eliminarDelCarrito(producto)
                        }
                    )
                }
            }
        }

    }
    Spacer(modifier = Modifier.height(4.dp))

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartTopBar(navController: NavController) {
    TopAppBar(
        title = { Text("Mi Carrito") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) { // Vuelve a la pantalla anterior (Home)
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
        }
    )
}

@Composable
fun CartBottomBar(total: Double, onCheckout: () -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total a Pagar:", style = MaterialTheme.typography.titleMedium)
            Text("$${"%.2f".format(total)}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                if (total > 0.0){
                    onCheckout()
                }else{
                    Toast.makeText(context, "No hay ariculos en el carrito", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = total > 0.0,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finalizar Compra") // Esto nos llevará al Punto 4: Formulario de Checkout/Pago

        }
    }
}

@Composable
fun CartItemRow(item: CarItem, onDelete:(com.example.mystore.model.Producto)-> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.producto.nombre,
                    style = MaterialTheme.typography.titleMedium)

                Text(
                    text = "Categoría: ${item.producto.categoriaId}",
                    style = MaterialTheme.typography.bodySmall
                )

                Text("Precio unitario: $${item.producto.precio}",
                    style = MaterialTheme.typography.bodySmall)

                Row(verticalAlignment = Alignment.CenterVertically) {
                Text( "Color", style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(colorFromNmae(item.variante.color))
                    )
                }


                Text("Talla: ${item.variante.talla}",
                    style = MaterialTheme.typography.bodySmall)
            }
            Text("x${item.quantity}",
                style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.width(16.dp))

            Text("$${"%.2f".format(item.producto.precio * item.quantity)}",
                style = MaterialTheme.typography.titleMedium)

            IconButton(onClick = {onDelete(item.producto)}) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar producto",
                    tint = Color.Black
                )
            }
        }
    }
}

fun colorFromNmae(name: String): Color{
    return when(name.lowercase()){
        "rojo" -> Color.Red
        "azul" -> Color.Blue
        "verde" -> Color.Green
        "amarillo" -> Color.Yellow
        "negro" -> Color.Black
        "blanco" -> Color.White
        "gris" -> Color.Gray
        "celeste" -> Color.Cyan
        "rosado" -> Color.Magenta
        "pistacho" -> Color(0xFF93C572)
        "única" -> Color.LightGray // color neutro
        else -> Color.LightGray
    }
}
