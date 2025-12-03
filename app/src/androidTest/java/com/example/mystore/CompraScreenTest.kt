package com.example.mystore

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.example.mystore.ui.theme.screen.CompraScreen
import com.example.mystore.viewModel.HomeViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CompraScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var dummyViewModel: HomeViewModel

    @Before
    fun setup(){
        val app = ApplicationProvider.getApplicationContext<Application>()
        dummyViewModel = HomeViewModel(app)
    }
    @Test
    fun CompraScreenTest_CamposCorrectos(){
        composeTestRule.setContent {
            CompraScreen(
                navController = rememberNavController(),
                homeViewModel = dummyViewModel
            )
        }
        composeTestRule.onNodeWithText("Nombre completo")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Correo electrónico")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Teléfono")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Calle")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Número")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Ciudad")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Comuna")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Ubicación")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Número de tarjeta")
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("MM/AA")
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("cvv")
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Pagar")
            .performScrollTo()
            .assertIsDisplayed()
            .assertIsNotEnabled()

        composeTestRule.onNodeWithText("Cancelar")
            .performScrollTo()
            .assertIsDisplayed()
            .assertIsEnabled()
    }
}