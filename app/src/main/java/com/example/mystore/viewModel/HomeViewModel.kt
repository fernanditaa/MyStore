package com.example.mystore.viewModel

import android.adservices.adid.AdId
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystore.data.dao.AppDatabase
import com.example.mystore.model.CarItem
import com.example.mystore.model.Categoria
import com.example.mystore.model.Producto
import com.example.mystore.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.mystore.model.Usuario
import com.example.mystore.repository.UsuarioRepository
import kotlinx.coroutines.flow.collectLatest

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val productoRepository: ProductoRepository = ProductoRepository()
    private val usuarioRepository: UsuarioRepository


<<<<<<< HEAD
    private val _categorias= MutableStateFlow<List<Categoria>>(emptyList())
            val categorias: StateFlow<List<Categoria>> = _categorias.asStateFlow()
    private val _usuarios= MutableStateFlow<List<Usuario>>(emptyList())
=======
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
>>>>>>> 90e7627716ce41fd62f2bd9d1a795ec41f73fdf6
    val usuarios: StateFlow<List<Usuario>> = _usuarios.asStateFlow()
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val producto: StateFlow<List<Producto>> = _productos.asStateFlow()

    private val _productosfiltrados= MutableStateFlow<List<Producto>>  (emptyList())
    var productosFiltrados: StateFlow<List<Producto>> = _productosfiltrados.asStateFlow()
    private val _carItem = MutableStateFlow<List<CarItem>>(emptyList())
    val carItem: StateFlow<List<CarItem>> = _carItem
    val carItemCount: StateFlow<Int> = _carItem.map { it.sumOf { item -> item.quantity } }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = 0
    )


    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        val usuarioDao = db.usuarioDao()
        usuarioRepository = UsuarioRepository(usuarioDao)

        fetchProducto()
        fetchUsuario()

        cargarUsuarioGuardado()
        cargarCategoria()
    }
<<<<<<< HEAD
    private fun fetchUsuario(){
        viewModelScope.launch {
            _isLoading.value= true
            try {
                usuarioRepository.obtenerUsuarios().collectLatest { lista ->
                    _usuarios.value = lista
                }
            }catch (e: Exception){
                print("Error al ingresar al usuario: ${e.localizedMessage}")
            }finally {
                _isLoading.value = false
            }
        }
    }
    private fun fetchProducto(){
        viewModelScope.launch {
            _isLoading.value = true
            try{
                _productos.value = productoRepository.getProducto()
                _productosfiltrados.value =_productos.value
            }catch (e: Exception){
                println("Error al cargar el producto: ${e.localizedMessage}")
            }finally {
                _isLoading.value = false
            }
        }
    }
    fun filtrarPorCategoria(categoriaId: Int){
        val  filtrados = if (categoriaId == 0){
            _productos.value
        }else{
            _productos.value.filter { it.categoriaId == categoriaId }
        }
        _productosfiltrados.value = filtrados
    }

    fun agregarCarrito(producto: Producto){
        val currentItems= _carItem.value.toMutableList()
        val existItem = currentItems.find { it.producto.id == producto.id }

        if (existItem != null){
            val updateItem = existItem.copy(quantity  = existItem.quantity + 1)
            currentItems.remove(existItem)
            currentItems.add(updateItem)
        }else{
            currentItems.add(CarItem(producto, 1))
        }
        _carItem.value = currentItems
    }
    fun eliminarDelCarrito(producto: Producto){
        val currentItems = _carItem.value.toMutableList()
        val updateItems = currentItems.filterNot {it.producto.id == producto.id}
        _carItem.value = updateItems
    }
    fun limpiarCarrito(){
        _carItem.value = emptyList()
    }
    fun registrarUsuario(nombre: String,
                         apellido: String,
                         telefono: String,
                         direccion: String,
                         correo: String,
                         contrasena:String,
                         onResult:( Boolean) -> Unit
    ){
        viewModelScope.launch {
            val nuevoUsuario = Usuario(
                nombre = nombre,
                apellido = apellido,
                telefono = telefono,
                direccion = direccion,
                correo = correo,
                contrasena = contrasena
            )
            val completado = usuarioRepository.registrarUsuario(nuevoUsuario)
            onResult(completado)
        }
    }
    fun validarLogin(
        correo: String,
        contrasena: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
        val usuario = usuarioRepository.validarLogin(correo, contrasena)
            _usuarioActual.value = usuario
            if(usuario != null){
                getApplication<Application>().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    .edit()
                    .putInt("usuario_id_actual", usuario.id)
                    .apply()
            }
        onResult (usuario != null)
        }
    }

    fun actualizarPerfil(
        nombre: String,
        telefono: String,
        direccion: String,
        correo: String,
        onResult: (Boolean) -> Unit
    ){
        viewModelScope.launch {
            val actual = _usuarioActual.value
            if (actual == null){
                onResult(false)
                return@launch
            }
            val actualiazdo = actual.copy(
                nombre = nombre,
                telefono = telefono,
                direccion= direccion,
                correo = correo,

            )
            try {
                usuarioRepository.actualizarUsuario(actualiazdo)
                _usuarioActual.value = actualiazdo
                onResult(true)
            }catch (e: Exception){
                onResult(false)
            }
        }
    }
    fun actualizarFotoPerfil(
        bitmap: Bitmap,
        onResult: (Boolean) -> Unit
    ){
        viewModelScope.launch {
            val actual = _usuarioActual.value
            if (actual == null){
                onResult(false)
                return@launch
            }
            try {
                val context = getApplication<Application>()
                val fileName = "foto_perfil_${actual.id}.png"

                context.openFileOutput(fileName, Context.MODE_PRIVATE).use { fos ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                }
                val actualizado = actual.copy(foto=  fileName)
                usuarioRepository.actualizarUsuario(actualizado)
                _usuarioActual.value = actualizado
                onResult(true)
            }catch (e: Exception){
                e.printStackTrace()
                onResult(false)
            }
        }
    }
=======

>>>>>>> 90e7627716ce41fd62f2bd9d1a795ec41f73fdf6
    private fun cargarUsuarioGuardado() {
        viewModelScope.launch {
            val prefs = getApplication<Application>()
                .getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

            val id = prefs.getInt("usuario_id_actual", -1)
            if (id != -1) {
                val usuario = usuarioRepository.obtenerUsuarioPorId(id)
                _usuarioActual.value = usuario
            }
        }
    }
    private fun cargarCategoria(){
        val lista = listOf(
            Categoria(1, "Amigurumis"),
            Categoria(2, "Souvenirs"),
            Categoria(3, "Gorros")
        )
        _categorias.value = lista
    }

    private fun fetchUsuario() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                usuarioRepository.obtenerUsuarios().collectLatest { lista ->
                    _usuarios.value = lista
                }
            } catch (e: Exception) {
                print("Error al ingresar al usuario: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchDescuento() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _descuentos.value = descuentoRepository.getDescuento()
            } catch (e: Exception) {
                print("Error al cargar el descuento: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchProducto() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _productos.value = productoRepository.getProducto()
            } catch (e: Exception) {
                println("Error al cargar el producto: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun agregarCarrito(producto: Producto) {
        val currentItems = _carItem.value.toMutableList()
        val existItem = currentItems.find { it.producto.id == producto.id }

        if (existItem != null) {
            val updateItem = existItem.copy(quantity = existItem.quantity + 1)
            currentItems.remove(existItem)
            currentItems.add(updateItem)
        } else {
            currentItems.add(CarItem(producto, 1))
        }
        _carItem.value = currentItems
    }

    fun eliminarDelCarrito(producto: Producto) {
        val currentItems = _carItem.value.toMutableList()
        val updateItems = currentItems.filterNot { it.producto.id == producto.id }
        _carItem.value = updateItems
    }

    fun limpiarCarrito() {
        _carItem.value = emptyList()
    }

    fun registrarUsuario(
        nombre: String,
        apellido: String,
        correo: String,
        contrasena: String,
        foto: Uri?,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val nuevoUsuario = Usuario(
                nombre = nombre,
                apellido = apellido,
                correo = correo,
                contrasena = contrasena
            )
            val completado = usuarioRepository.registrarUsuario(nuevoUsuario)
            onResult(completado)

            if (completado) {
                val usuarioGuardado = usuarioRepository.validarLogin(correo, contrasena)

                _usuarioActual.value = usuarioGuardado

                usuarioGuardado?.let { u ->
                    getApplication<Application>()
                        .getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                        .edit()
                        .putInt("usuario_id_actual", u.id)
                        .apply()
                }
            }
            onResult(completado)
        }
    }

        fun validarLogin(
            correo: String,
            contrasena: String,
            onResult: (Boolean) -> Unit
        ) {
            viewModelScope.launch {
                val usuario = usuarioRepository.validarLogin(correo, contrasena)
                _usuarioActual.value = usuario
                if (usuario != null) {
                    getApplication<Application>().getSharedPreferences(
                        "app_prefs",
                        Context.MODE_PRIVATE
                    )
                        .edit()
                        .putInt("usuario_id_actual", usuario.id)
                        .apply()
                }
                onResult(usuario != null)
            }
        }

        fun actualizarPerfil(
            nombre: String,
            correo: String,
            telefono: String,
            direccion: String,
            onResult: (Boolean) -> Unit
        ) {
            viewModelScope.launch {
                val actual = _usuarioActual.value
                if (actual == null) {
                    onResult(false)
                    return@launch
                }
                val actualiazdo = actual.copy(
                    nombre = nombre,
                    correo = correo,
                    telefono = telefono,
                    direccion = direccion,
                )
                try {
                    usuarioRepository.actualizarUsuario(actualiazdo)
                    _usuarioActual.value = actualiazdo
                    onResult(true)
                } catch (e: Exception) {
                    onResult(false)
                }
            }
        }

        fun actualizarFotoPerfil(
            bitmap: Bitmap,
            onResult: (Boolean) -> Unit
        ) {
            viewModelScope.launch {
                val actual = _usuarioActual.value
                if (actual == null) {
                    onResult(false)
                    return@launch
                }
                try {
                    val context = getApplication<Application>()
                    val fileName = "foto_perfil_${actual.id}.png"

                    context.openFileOutput(fileName, Context.MODE_PRIVATE).use { fos ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    }
                    val actualizado = actual.copy(foto = fileName)
                    usuarioRepository.actualizarUsuario(actualizado)
                    _usuarioActual.value = actualizado
                    onResult(true)
                } catch (e: Exception) {
                    e.printStackTrace()
                    onResult(false)
                }
            }
        }
    }