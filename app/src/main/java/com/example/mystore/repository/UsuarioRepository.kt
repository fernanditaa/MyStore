package com.example.mystore.repository

import androidx.room.Query
import com.example.mystore.data.dao.UsuarioDao
import com.example.mystore.data.dao.remote.LoginRequest
import com.example.mystore.data.dao.remote.RetrofitClient
import com.example.mystore.model.Usuario
import kotlinx.coroutines.flow.Flow

class UsuarioRepository (private val usuarioDao: UsuarioDao){

    private val api = RetrofitClient.instance

    //Registro de usuario APi
    suspend fun registrarUsuario(usuario: Usuario): Boolean{
        return try {

            println("API_TEST: Intentando enviar usuario: ${usuario.nombre} al servidor...")
            val  respuestaApi = api.registrarUsuarios(usuario)

            if (respuestaApi.isSuccessful){
                val usuarioCreado = respuestaApi.body()
                println("API_TEST: ¡ÉXITO! El servidor respondió: $usuarioCreado")

                // Guardar en Room
                usuarioDao.insertarUsuario(usuarioCreado!!)

                return true
            }else{
                println("API_TEST: Error del servidor. Código: ${respuestaApi.code()}")
                false
            }
        }catch (e: Exception){

            println("API_TEST: Fallo crítico de conexión: ${e.message}")
            false
        }
    }
    //Login real de la api
    suspend fun validarLogin(email: String, contrasena: String): Usuario?{
        return try {
            val response = api.login(LoginRequest(email, contrasena))

            if(response.isSuccessful){
                val usuario = response.body()
                println("API_TEST: Login OK-> $usuario")

                usuarioDao.insertarUsuario(usuario!!)
                return usuario
            }else{
                println("API_TEST: Loogin falló. Código; ${response.code()}")
                null
            }
        }catch (e: Exception){
            println("API_TEST: Error de coneccion en login: ${e.message}")
            null
        }
    }
    suspend fun actualizarUsuario(usuario: Usuario){
        try {
            val response = api.actualizarUsuario(usuario.id.toLong(), usuario)
            if (response.isSuccessful){
                usuarioDao.actualizarUsuario(usuario)
                println("API_TEST: Perfil actualizado corectamente")
            }else{
                println("API_TEST: Error actualizando perfil: ${response.code()}")
            }
        }catch (e: Exception){
            println("API_TEST: Error critico al actualizar perfil: ${e.message}")
        }
    }
    suspend fun obtenerUsuarios(): List<Usuario> {
        return try {
            val resp = api.obtenerUsuarios()
            resp.body()?: emptyList()
        } catch (e: Exception) {
            println("API_TEST: Error obteniendo usuarios: ${e.message}")
            emptyList()
        }
    }

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun obtenerUsuarioPorId(id: Int): Usuario?{
        return try {
            val resp = api.obtenerUsuario(id.toLong())
            if(resp.isSuccessful){
                resp.body()
            }else{
                null
            }
        }catch (e: Exception){
            println("API_TEST: Error obteniendo usuario por id: ${e.message}")
            null
        }
    }

    fun obtenerUsuarioLocal(): Flow<List<Usuario>> = usuarioDao.obtenerUsuarios()

    suspend fun obtenerUsuarioLocal(id: Int): Usuario? {
        return usuarioDao.obtenerUsuarioPorId(id)
    }
}

