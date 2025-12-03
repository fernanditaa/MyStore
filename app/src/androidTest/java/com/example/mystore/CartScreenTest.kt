package com.example.mystore

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.example.mystore.model.Producto
import com.example.mystore.ui.theme.screen.CartScreen
import com.example.mystore.ui.theme.screen.CompraScreen
import com.example.mystore.viewModel.HomeViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var dummyViewModel: HomeViewModel

    @Before
    fun setup(){
        val app = ApplicationProvider.getApplicationContext<Application>()
        dummyViewModel = HomeViewModel(app)
    }

    @Test
    fun cartScreenVacio() {

        composeTestRule.setContent {
            CartScreen(
                navController = rememberNavController(),
                viewModel = dummyViewModel
            )
        }

        composeTestRule.onNodeWithText("Mi Carrito")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Su carrito está vacío.")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Finalizar Compra")
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun cartScreenConProdutco(){

        val producto = Producto(
            1,
            "Turbo Abuela",
            "Personaje del anime 'DanDaDan' ",
            "18 cm",
            35000.0,
            0,
            1
        )

        dummyViewModel.agregarCarrito(producto)

        composeTestRule.setContent {
            CartScreen(
                navController = rememberNavController(),
                viewModel = dummyViewModel
            )
        }

        composeTestRule.onNodeWithText("Turbo Abuela")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("x1")
            .assertIsDisplayed()


        composeTestRule.onNodeWithText("Finalizar Compra")
            .assertIsDisplayed()
            .assertIsEnabled()

        composeTestRule.onNodeWithContentDescription("Eliminar producto")
            .assertIsDisplayed()
    }
}