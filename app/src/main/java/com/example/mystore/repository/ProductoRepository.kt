package com.example.mystore.repository

import com.example.mystore.model.Producto
import com.example.mystore.R
import com.example.mystore.data.dao.remote.RetrofitClient

class ProductoRepository {

    private val api = RetrofitClient.instance

    //obtenet todos los productos
    suspend fun obtenerProductos(): List<Producto>{
        return try {
            val resp = api.obtenerProductos()
            resp.body()?: emptyList()
        }catch (e: Exception){
            println("API_TEST: Error obteniendo productos: ${e.message}")
            emptyList()
        }
    }

    //por id
    suspend fun obtenerProductoPorId(id: Long): Producto?{
        return try {
            val  resp = api.obtenerPoductoPorId(id)
            if (resp.isSuccessful) resp.body() else null
        }catch (e: Exception){
            println("API_TEST: Error al obtener producto por id ${e.message}")
            null
        }
    }

    //crear producto

    suspend fun crearProducto(producto: Producto): Producto?{
        return try {
            val resp = api.crearProducto(producto)
            if (resp.isSuccessful) resp.body() else null
        }catch (e: Exception){
            println("API_TEST: Error al crear productos ${e.message}")
            null
        }
    }

    //actualizar
    suspend fun actualizarProducto(id: Long, producto: Producto): Producto?{
        return try {
            val resp = api.actualizarProducto(id, producto)
            if (resp.isSuccessful) resp.body() else null
        }catch (e: Exception){
            println("API_TEST: Error al actualizar productos ${e.message}")
            null
        }
    }

    //Eliminar
    suspend fun eliminarProducto(id: Long): Boolean{
        return try {
            val resp = api.eliminarProducto(id)
            resp.isSuccessful
        }catch (e: Exception){
            println("API_TEST: Error al eliminar productos ${e.message}")
            false
        }
    }
}