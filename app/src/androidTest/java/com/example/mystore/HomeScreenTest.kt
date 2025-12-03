package com.example.mystore

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.example.mystore.ui.theme.screen.CompraScreen
import com.example.mystore.ui.theme.screen.HomeScreen
import com.example.mystore.ui.theme.screen.PerfilScreen
import com.example.mystore.viewModel.HomeViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var dummyViewModel: HomeViewModel

    @Before
    fun setup(){
        val app = ApplicationProvider.getApplicationContext<Application>()
        dummyViewModel = HomeViewModel(app)
    }
    @Test
    fun HomeScreenTest() {
        composeTestRule.setContent {
            HomeScreen(
                navController = rememberNavController(),
                viewModel = dummyViewModel
            )
        }

        composeTestRule.onNodeWithText("KatHub")
            .assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Abrir Menú")
            .assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Carrito")
            .assertIsDisplayed()
    }

    @Test
    fun abreElMenuYPruebaSusDatos(){
        composeTestRule.setContent {
            HomeScreen(
                navController = rememberNavController(),
                viewModel = dummyViewModel
            )
        }

        composeTestRule.onNodeWithContentDescription("Abrir Menú")
            .performClick()

        composeTestRule.onNodeWithText("Menú KatHub")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Inicio")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Categorias")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Mi Perfil")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Contacto")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Cerrar Sesión")
            .assertIsDisplayed()
    }
}