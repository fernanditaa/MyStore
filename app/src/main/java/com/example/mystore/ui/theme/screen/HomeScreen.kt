package com.example.mystore.ui.theme.screen

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mystore.model.Producto
import com.example.mystore.viewModel.HomeViewModel
import kotlinx.coroutines.launch
import androidx.compose.material3.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {

    val producto by viewModel.productos.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val cartCount by viewModel.carItemCount.collectAsState()
    val categoria by viewModel.categorias.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    //variable para controlar el submenu de categoria
    var categoriasExpandido by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Menú KatHub",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                Divider()

                DrawerItem(
                    title = "Inicio",
                    icon = Icons.Default.Home,
                    onClick = { scope.launch { drawerState.close()}
                        navController.navigate("Home"){
                            popUpTo (navController.graph.startDestinationId){inclusive = true}
                        }
                    }
                )
                DrawerItem(
                    title = "Categorias",
                    icon = Icons.Default.Dashboard,
                    onClick = { categoriasExpandido = !categoriasExpandido }
                )
                if(categoriasExpandido){
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        categorias.forEach { cat ->
                            NavigationDrawerItem(
                                    label ={Text(cat.nombre)},
                                    selected = false,
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    //para navegar a las categorias
                                    val  encodedName = Uri.encode(cat.nombre)
                                    navController.navigate("category/${cat.id}/$encodedName"){
                                        launchSingleTop = true
                                    }
                                },
                                icon = {Icon(Icons.Default.Category, contentDescription = cat.nombre)},
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        }
                    }
                }

                DrawerItem(
                    title = "Mi Perfil",
                    icon = Icons.Default.Person,
                    onClick = { scope.launch { drawerState.close() }
                        navController.navigate("Mi Perfil"){
                            popUpTo (navController.graph.startDestinationId){inclusive = true}
                        }
                    }
                )
                DrawerItem(
                    title = "Contacto",
                    icon = Icons.Default.Contacts,
                    onClick = { scope.launch { drawerState.close()} /* navegar a profile */ }
                )

                DrawerItem(
                    title = "Cerrar Sesión",
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                )


            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("KatHub") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir Menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate("carrito") }) {
                            BadgedBox(
                                badge = {
                                    if (cartCount > 0) {
                                        Badge { Text(cartCount.toString()) }
                                    }
                                }
                            ) {
                                Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                } else {
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {


                        items(producto) { p ->
                            ProductCard(
                                producto = p,
                                onAddToCart = { viewModel.agregarCarrito(p) }
                            )
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}
// crea el item del menú lateral (la hambuerguesa)
@Composable
fun DrawerItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(title) },
        selected = false,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = title) },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable
fun ProductCard(producto: Producto, onAddToCart: (Producto) -> Unit) {

    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            var expanded by remember { mutableStateOf(false) }
            Image(
                painter = painterResource(id = producto.imagen),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(if (expanded) 500.dp else 150.dp)
                    .clickable { expanded = !expanded },
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = producto.descripcion,
                style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = producto.medida,
                style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$${producto.precio}",
                style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            IconButton (
                onClick = {onAddToCart(producto)
                    Toast.makeText(context, "${producto.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
                },
            ){
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Añadir al carrito"

                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    categoryId: Int,
    categoryName: String,
    viewModel: HomeViewModel,
    navController: NavController
){
    val productos by viewModel.productos.collectAsState()//lista completa de los productos
    val productosFiltrados = productos.filter { it.categoriaId == categoryId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(categoryName)},
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate("home"){popUpTo(navController.graph.startDestinationId)} }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription= "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (productosFiltrados.isEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ){
                Text("No hay productos en esta categoría")
            }
        }else{
            LazyColumn(contentPadding = paddingValues) {
                items(productosFiltrados){ producto ->
                    ProductCard(producto = producto, onAddToCart = {viewModel.agregarCarrito(producto)})
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

