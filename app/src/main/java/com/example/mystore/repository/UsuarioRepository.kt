package com.example.mystore.repository

import com.example.mystore.data.dao.UsuarioDao
import com.example.mystore.model.Usuario
import kotlinx.coroutines.flow.Flow

class UsuarioRepository (private val usuarioDao: UsuarioDao){

    suspend fun registrarUsuario(usuario: Usuario): Boolean{
        val existente = usuarioDao.obtenerPorCorreo(usuario.correo)
        if(existente !=null){
            return false
        }
        usuarioDao.insertarUsuario(usuario)
        return true
    }
    suspend fun validarLogin(correo: String, contrasena: String): Usuario?{
        return usuarioDao.login(correo, contrasena)
    }
    suspend fun actualizarUsuario(usuario: Usuario){
        usuarioDao.actualizarUsuario(usuario)
    }
    fun obtenerUsuarios(): Flow<List<Usuario>> = usuarioDao.obtenerUsuarios()
}
