package com.example.mystore.repository

import com.example.mystore.model.Producto
import com.example.mystore.R
class ProductoRepository {

    private val Productos = listOf(
        Producto(1,"TurboAbuela","Personaje del anime 'DanDaDan' ", 35.000,R.drawable.turboabuela),
        Producto(2,"Corazon","Souvenir, recuerdo para Matrimonio, todos los souvenris son por docena", 13.000, R.drawable.corazon),
        Producto(3,"Boina","Tejido en Lana Hipoalergenica para todo tipo de piel", 28.000, R.drawable.boina)
    )

    suspend fun getProducto(): List<Producto>{
        kotlinx.coroutines.delay(1000)
        return Productos
    }

    suspend fun getProductoById(id: Int): Producto?{
        kotlinx.coroutines.delay(500)
        return Productos.find { it.id == id }
    }
}