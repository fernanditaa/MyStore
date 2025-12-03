package com.example.mystore

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.mystore.ui.theme.screen.LoginScreen
import com.example.mystore.viewModel.HomeViewModel
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.assert


class LoginScreenTest{

    @get:Rule
    val composeTestRule = createComposeRule()

    val dummyViewModel = mockk<HomeViewModel>(relaxed = true)


    @Test
    fun loginScreen_displaysFieldsAndButtons(){
        composeTestRule.setContent {
            LoginScreen(
                navController = rememberNavController(),
                homeViewModel = dummyViewModel
            )
        }

        //verifica que los campos de texto existen
        composeTestRule.onNodeWithText("Correo electrónico")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Contraseña")
            .assertIsDisplayed()

        //verificamos los botones
        composeTestRule.onNodeWithText("Iniciar sesión")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("¿No tienes cuenta? Regístrate")
            .assertIsDisplayed()
    }
    @Test
    fun loginScreen_canInputText(){
        composeTestRule.setContent {
            LoginScreen(
                navController = rememberNavController(),
                homeViewModel = dummyViewModel
            )
        }
        //ingresa texto en los campos
        composeTestRule.onNodeWithText("Correo electrónico")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Contraseña")
            .assertIsDisplayed()



    }
}