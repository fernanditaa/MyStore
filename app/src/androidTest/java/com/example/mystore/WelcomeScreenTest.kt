package com.example.mystore

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.mystore.ui.theme.screen.WelcomeScreen
import org.junit.Rule
import org.junit.Test

class WelcomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun  welcomeScrenn_muestraElementosCorrectos(){
        composeTestRule.setContent {
            WelcomeScreen(
                navController = rememberNavController()
            )
        }
        //verificando textos principales
        composeTestRule.onNodeWithText("KatHub")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Lo imaginas, lo creamos")
            .assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Fondo de la tienda")
            .assertIsDisplayed()
    }
}