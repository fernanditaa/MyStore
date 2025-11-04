package com.example.mystore.repository

import com.example.mystore.model.Producto

class ProductoRepository {

    private val Productos = listOf(
        Producto(1,"Chopper","Amigurumi", 35.000),
        Producto(2,"Corazon","Souvenir", 13.000),
        Producto(1,"Boina","Lana rosada", 28.000)
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