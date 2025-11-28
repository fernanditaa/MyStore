package com.example.mystore.data.dao.remote

import com.example.mystore.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("users")
    suspend fun obtenerUsuarios(): Response<List<Usuario>>

    @POST("users")
    suspend fun crearUsuario(@Body usuario: Usuario): Response<Usuario>
}