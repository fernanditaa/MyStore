package com.example.mystore.repository

import com.example.mystore.data.dao.UsuarioDao
import com.example.mystore.data.dao.remote.RetrofitClient
import com.example.mystore.model.Usuario
import kotlinx.coroutines.flow.Flow

class UsuarioRepository (private val usuarioDao: UsuarioDao){

    private val api = RetrofitClient.instance
    suspend fun registrarUsuario(usuario: Usuario): Boolean{
        return try {

            println("API_TEST: Intentando enviar usuario: ${usuario.nombre} al servidor...")
            val  respuestaApi = api.crearUsuario(usuario)
            if (respuestaApi.isSuccessful){
                val respuestaCuerpo = respuestaApi.body()
                //respuesta de exito
                println("API_TEST: ¡ÉXITO! El servidor respondió: $respuestaCuerpo")

                // Guardar en Room
                val resultadoLocal = usuarioDao.insertarUsuario(usuario)

                //-1 significa que el correo ya existe en la base de datos
                if (resultadoLocal == -1L) {
                    println("API_TEST: Correo ya existe en la base local. No insertado.")
                    return false
                }

                println("API_TEST: Usuario guardado en Room con ID: $resultadoLocal")
                return true
            }else{
                //respuesta de error
                println("API_TEST: Error del servidor. Código: ${respuestaApi.code()}")
                false
            }
        }catch (e: Exception){

            println("API_TEST: Fallo crítico de conexión: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    suspend fun validarLogin(correo: String, contrasena: String): Usuario?{
        return usuarioDao.login(correo, contrasena)
    }
    suspend fun actualizarUsuario(usuario: Usuario){
        usuarioDao.actualizarUsuario(usuario)
    }
    fun obtenerUsuarios(): Flow<List<Usuario>> = usuarioDao.obtenerUsuarios()

    suspend fun obtenerUsuarioPorId(id: Int): Usuario? {
        return usuarioDao.obtenerUsuarioPorId(id)
    }

}
