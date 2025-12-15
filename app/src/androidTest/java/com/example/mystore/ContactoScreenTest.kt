package com.example.mystore

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.example.mystore.ui.theme.screen.ContactoScreen
import com.example.mystore.viewModel.HomeViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ContactoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var dummyViewModel: HomeViewModel

    @Before
    fun setup(){
        val app = ApplicationProvider.getApplicationContext<Application>()
        dummyViewModel = HomeViewModel(app)
    }
    @Test
    fun ContactoScreenTest(){
        composeTestRule.setContent {
            ContactoScreen(
                navController = rememberNavController(),
                homeViewModel =  dummyViewModel
            )
        }

        composeTestRule.onNodeWithText("Contacto")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(
            "Si necesitas comunicarte con nosotros solo dejanos un mensaje describiendo tu problema y en la brevedad nos comunicaremos contigo")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Nombres")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Correo electrónico")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Mensaje")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("enviar mensaje")
            .assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Volver")
            .assertIsDisplayed()
    }


}