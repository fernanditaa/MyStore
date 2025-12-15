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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mystore.ui.theme.MyStoreTheme
import com.example.mystore.ui.theme.screen.CartScreen
import com.example.mystore.ui.theme.screen.CompraScreen
import com.example.mystore.ui.theme.screen.HomeScreen
import com.example.mystore.ui.theme.screen.LoginScreen
import com.example.mystore.ui.theme.screen.WelcomeScreen
import com.example.mystore.viewModel.HomeViewModel
import com.example.mystore.ui.theme.screen.PerfilScreen
import com.example.mystore.ui.theme.screen.RegistroUsuarioScreen
import com.example.mystore.ui.theme.screen.CategoryScreen
import com.example.mystore.ui.theme.screen.ContactoScreen

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
                        alpha = 0.5f //hacemos la imagen mas transparente
                    )
                    NavHost(
                        navController = navController,
                        startDestination = "welcome")
                    {
                        composable("welcome"){
                            WelcomeScreen(navController = navController)
                        }

                        composable("login"){
                            LoginScreen(navController = navController,
                                homeViewModel = sharedHomeViewModel)
                        }
                        composable("registro"){
                            RegistroUsuarioScreen(navController = navController,
                                homeViewModel = sharedHomeViewModel)
                        }

                        composable("home"){
                            HomeScreen(viewModel = sharedHomeViewModel,
                                navController = navController)
                        }

                        composable("carrito") {
                            CartScreen(navController = navController,
                                viewModel = sharedHomeViewModel)
                        }
                        composable("Mi Perfil"){
                            PerfilScreen(navController = navController, homeViewModel = sharedHomeViewModel)
                        }
                        composable("finalizar compra"){
                            CompraScreen(navController = navController,
                                homeViewModel = sharedHomeViewModel)
                        }
                        composable ("Contacto"){
                            ContactoScreen(navController = navController, homeViewModel = sharedHomeViewModel)
                        }
                        composable("category/{categoryId}/{categoryName}",
                            arguments = listOf(
                                navArgument("categoryId"){type = NavType.IntType},
                                navArgument("categoryName"){type = NavType.StringType}
                            )
                        ){backStackEntry ->
                            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
                            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
                            CategoryScreen(
                                categoryId = categoryId,
                                categoryName = categoryName,
                                viewModel = sharedHomeViewModel,
                                navController = navController
                            )

                        }

                    }
                }
            }
        }
    }
}