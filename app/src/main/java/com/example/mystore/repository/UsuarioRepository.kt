package com.example.mystore.repository

import com.example.mystore.model.Usuario

class UsuarioRepository {

    // Lista en memoria donde se guardan los usuarios registrados
    private val usuarios = mutableListOf<Usuario>()

    // Registrar usuario
    fun registrarUsuario(usuario: Usuario): Boolean {
        // Si ya existe un usuario con el mismo correo, no lo agrega
        if (usuarios.any { it.correo == usuario.correo }) {
            return false
        }
        usuarios.add(usuario)
        return true
    }

    // Validar login
    fun validarLogin(correo: String, contrasena: String): Usuario? {
        return usuarios.find { it.correo == correo && it.contrasena == contrasena }
    }

    // Obtener lista de usuarios (por si la necesitas)
    fun obtenerUsuarios(): List<Usuario> = usuarios
}
