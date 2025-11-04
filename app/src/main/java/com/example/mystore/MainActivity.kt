package com.example.mystore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mystore.ui.theme.MyStoreTheme
import com.example.mystore.ui.theme.screen.HomeScreen
import com.example.mystore.ui.theme.screen.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyStoreTheme {
                val  navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "Welcome"){
                    composable("Welcome"){
                        WelcomeScreen(navController = navController)
                    }

                    composable("Home"){
                        HomeScreen(viewModel = viewModel ())
                    }
                }
            }
        }
    }
}
