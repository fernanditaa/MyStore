package com.example.mystore.repository

import com.example.mystore.model.Producto
import com.example.mystore.R
class ProductoRepository {

    private val Productos = listOf(
        Producto(1,"TurboAbuela","Personaje del anime 'DanDaDan' ", "18cm",35000.0,R.drawable.turboabuela),
        Producto(2,"Souvenir","Souvenir, recuerdo en forma de corazón para Matrimonio, todos los souvenris son por docena", "8cm",13000.0, R.drawable.corazon),
        Producto(3,"Boina","Tejido en Lana Hipoalergenica para todo tipo de piel", "22cm de circunferencia", 28000.0, R.drawable.boina)
    )
//Devolvemos la lista de los productos con los datos simulados
    suspend fun getProducto(): List<Producto>{
        kotlinx.coroutines.delay(1000)// tiempo de carga de 1 segundo de espera
        return Productos // devuelve los productos
    }

    // devuelve el producto por el ID
    suspend fun getProductoById(id: Int): Producto?{
        kotlinx.coroutines.delay(500)
        return Productos.find { it.id == id }// busca el producto por el ID
    }
}