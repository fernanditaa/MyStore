package com.example.mystore

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.example.mystore.ui.theme.screen.PerfilScreen
import com.example.mystore.viewModel.HomeViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PerfilscreenTest {

    @get:Rule
    val  composeTestRule = createComposeRule()

    private lateinit var dummyViewModel: HomeViewModel

    @Before
    fun setup(){
        val app = ApplicationProvider.getApplicationContext<Application>()
        dummyViewModel = HomeViewModel(app)
    }

    @Test
    fun perfilScreenCargarDatos(){
        composeTestRule.setContent {
            PerfilScreen(
                navController = rememberNavController(),
                homeViewModel = dummyViewModel
            )
        }
        //Campos de texto
        composeTestRule.onNodeWithText("Nombre")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Correo")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Telefono")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Dirección")

        //Botones
        composeTestRule.onNodeWithText("Editar perfil")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Volver")
            .assertIsDisplayed()
    }
}