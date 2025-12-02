package com.example.mystore.model

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val medida: String,
    val precio: Double,
    val imagen: Int,
    val categoriaId: Int
)
