package com.example.mystore.data.dao.remote

import com.example.mystore.model.Producto
import com.example.mystore.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    //Login
    @POST("api/usuarios/login")
    suspend fun login(@Body request: LoginRequest): Response<Usuario>

    //Registro
    @POST("api/usuarios/registro")
    suspend fun registrarUsuarios(@Body usuario: Usuario): Response<Usuario>

    //Obtener todos
    @GET("api/usuarios")
    suspend fun obtenerUsuarios(): Response<List<Usuario>>

    //Obtener por id
    @GET("api/usuarios/{id}")
    suspend fun obtenerUsuario(@Path("id")id: Long): Response<Usuario>

    //Actualizacion
    @PATCH("api/usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: Long,
        @Body usuario: Usuario
    ): Response<Usuario>

    @GET("/api/productos")
    suspend fun obtenerProductos(): Response<List<Producto>>

    @GET("/api/producatos/{id}")
    suspend fun obtenerPoductoPorId(@Path("id")id: Long): Response<Producto>

    @POST("/api/productos")
    suspend fun crearProducto(@Body producto: Producto): Response<Producto>

    @PUT("/api/productos/{id}")
    suspend fun actualizarProducto(
        @Path("id")id: Long,
        @Body producto: Producto
    ): Response<Producto>

    @PATCH("/api/productos{id}")
    suspend fun actualizarproductoParcial(
        @Path("id")id: Long,
        @Body producto: Producto
    ): Response<Producto>

    @DELETE("/api/producto/{id}")
    suspend fun eliminarProducto(
        @Path("id")id: Long
    ): Response<Unit>
}