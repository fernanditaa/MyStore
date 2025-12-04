package com.example.mystore.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios", indices = [Index(value = ["email"], unique = true)])
data class Usuario(

    @PrimaryKey(autoGenerate = true)
    val id: Int= 0,
    val nombre: String,
    val apellido: String,
    val email: String,
    val contrasena: String,
    val telefono: String? = null,
    val direccion: String? = null,
    val foto: String? = null
)
