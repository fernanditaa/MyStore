package com.example.mystore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mystore.ui.theme.MyStoreTheme
import com.example.mystore.ui.theme.screen.CartScreen
import com.example.mystore.ui.theme.screen.CompraScreen
import com.example.mystore.ui.theme.screen.DescuentosScreen
import com.example.mystore.ui.theme.screen.HomeScreen
import com.example.mystore.ui.theme.screen.LoginScreen
import com.example.mystore.ui.theme.screen.WelcomeScreen
import com.example.mystore.viewModel.HomeViewModel
import com.example.mystore.ui.theme.screen.PerfilScreen
import com.example.mystore.ui.theme.screen.RegistroUsuarioScreen


// Aca manejamos las rutas, donde nos lleva cada pagina
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyStoreTheme {
                val  navController = rememberNavController()

                val sharedHomeViewModel: HomeViewModel = viewModel()
                Box(
                    modifier = Modifier.fillMaxSize()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.imagen1),
                        contentDescription = "Fondo de la tienda",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = 0.3f //hacemos la imagen mas transparente
                    )
                    NavHost(
                        navController = navController,
                        startDestination = "welcome")
                    {
                        composable("welcome"){
                            WelcomeScreen(navController = navController)
                        }

                        composable("login"){
                            LoginScreen(navController = navController)
                        }
                        composable("registro"){
                            RegistroUsuarioScreen(navController = navController)
                        }

                        composable("home"){
                            HomeScreen(viewModel = sharedHomeViewModel, navController = navController)
                        }

                        composable("carrito") {
                            CartScreen(navController = navController, sharedHomeViewModel)
                        }
                        composable("Mi Perfil"){
                            PerfilScreen(navController = navController)
                        }
                        composable("finalizar compra"){
                            CompraScreen(navController = navController, viewModel = sharedHomeViewModel)
                        }
                        composable ("descuentos"){
                            DescuentosScreen(navController = navController, viewModel = sharedHomeViewModel)
                        }
                    }
                }
            }
        }
    }
}

