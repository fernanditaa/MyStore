package com.example.mystore

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.example.mystore.ui.theme.screen.RegistroUsuarioScreen
import com.example.mystore.viewModel.HomeViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegistroUsuarioScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var dummyViewModel: HomeViewModel

    @Before
    fun setup(){
        val app = ApplicationProvider.getApplicationContext<Application>()
        dummyViewModel = HomeViewModel(app)
    }
    @Test
    fun registroUsuarioScreen_muestraCamposCorrectos(){
        composeTestRule.setContent {
            RegistroUsuarioScreen(
                navController = rememberNavController(),
                homeViewModel = dummyViewModel
            )
        }
        //campos visibles
        composeTestRule.onNodeWithText("Nombre")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Apellidos")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Teléfono")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Dirección")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("e-mail")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Contraseña")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Confirmar contraseña")
            .assertIsDisplayed()

        //Boton registrar
        composeTestRule.onNodeWithText("Registrar")
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }
}