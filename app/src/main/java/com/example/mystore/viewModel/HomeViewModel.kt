package com.example.mystore.viewModel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystore.data.dao.AppDatabase
import com.example.mystore.model.CarItem
import com.example.mystore.model.Categoria
import com.example.mystore.model.Producto
import com.example.mystore.model.Usuario
import com.example.mystore.repository.ProductoRepository
import com.example.mystore.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

class HomeViewModel(application: Application) : AndroidViewModel(application) {


    private val productoRepository: ProductoRepository = ProductoRepository()
    private val usuarioRepository: UsuarioRepository

    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())
    val categorias: StateFlow<List<Categoria>> = _categorias.asStateFlow()

    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios.asStateFlow()

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    private val _productosFiltrados = MutableStateFlow<List<Producto>>(emptyList())
    val productosFiltrados: StateFlow<List<Producto>> = _productosFiltrados.asStateFlow()

    private val _carItem = MutableStateFlow<List<CarItem>>(emptyList())
    val carItem: StateFlow<List<CarItem>> = _carItem

    val carItemCount: StateFlow<Int> = _carItem.map { it.sumOf { item -> item.quantity } }
        .stateIn(
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

        fetchProductos()
        fetchUsuarios()
        cargarUsuarioGuardado()
        cargarCategorias()
    }

    // ---- FUNCIONES DE USUARIO ----

    private fun fetchUsuarios() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                usuarioRepository.obtenerUsuarios().collectLatest { lista ->
                    _usuarios.value = lista
                }
            } catch (e: Exception) {
                println("Error al cargar usuarios: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun registrarUsuario(
        nombre: String,
        apellido: String,
        telefono: String,
        direccion: String,
        correo: String,
        contrasena: String,
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
            if (completado) {
                val usuarioGuardado = usuarioRepository.validarLogin(correo, contrasena)
                _usuarioActual.value = usuarioGuardado
                usuarioGuardado?.let { u ->
                    getApplication<Application>().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                        .edit()
                        .putInt("usuario_id_actual", u.id)
                        .apply()
                }
            }
            onResult(completado)
        }
    }

    fun validarLogin(correo: String, contrasena: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val usuario = usuarioRepository.validarLogin(correo, contrasena)
            _usuarioActual.value = usuario
            if (usuario != null) {
                getApplication<Application>().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
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
            val actualizado = actual.copy(
                nombre = nombre,
                correo = correo,
                telefono = telefono,
                direccion = direccion
            )
            try {
                usuarioRepository.actualizarUsuario(actualizado)
                _usuarioActual.value = actualizado
                onResult(true)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }

    fun actualizarFotoPerfil(bitmap: Bitmap, onResult: (Boolean) -> Unit) {
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

    private fun cargarCategorias() {
        val lista = listOf(
            Categoria(1, "Amigurumis"),
            Categoria(2, "Souvenirs"),
            Categoria(3, "Gorros")
        )
        _categorias.value = lista
    }

    // ---- FUNCIONES DE PRODUCTO ----

    private fun fetchProductos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _productos.value = productoRepository.getProducto()
                _productosFiltrados.value = _productos.value
            } catch (e: Exception) {
                println("Error al cargar productos: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filtrarPorCategoria(categoriaId: Int) {
        val filtrados = if (categoriaId == 0) {
            _productos.value
        } else {
            _productos.value.filter { it.categoriaId == categoriaId }
        }
        _productosFiltrados.value = filtrados
    }

    // ---- FUNCIONES DE CARRITO ----

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
        _carItem.value = _carItem.value.filterNot { it.producto.id == producto.id }
    }

    fun limpiarCarrito() {
        _carItem.value = emptyList()
    }
}
