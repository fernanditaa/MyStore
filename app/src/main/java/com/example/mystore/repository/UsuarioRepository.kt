package com.example.mystore.repository

import com.example.mystore.model.Usuario

class UsuarioRepository {
    private val usuarios = mutableListOf<Usuario>()

    //Registramos el usuario
    fun registrarUsuario(usuario: Usuario): Boolean{
        //para que no se repita el correo
        if (usuarios.any{it.correo == usuario.correo}) return false
        usuarios.add(usuario)
        return true
    }
    //validamos ingreso
    fun validarLogin(correo: String, contrasena: String): Usuario? {
        return usuarios.find { it.correo == correo && it.contrasena == contrasena}
    }
    fun obtenerUsuarios(): List<Usuario> = usuarios


}