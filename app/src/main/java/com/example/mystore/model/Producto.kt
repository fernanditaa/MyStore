package com.example.mystore.model

data class Producto(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int?,
    val medida: String,
    val categoria: Int,
    val imagenUrl: String?
)
