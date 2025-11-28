package com.example.mystore.viewModel

import android.net.Uri
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystore.data.dao.AppDatabase
import com.example.mystore.model.CarItem
import com.example.mystore.model.Producto
import com.example.mystore.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.mystore.model.Descuento
import com.example.mystore.model.Usuario
import com.example.mystore.repository.DescuentoRepository
import com.example.mystore.repository.UsuarioRepository
import kotlinx.coroutines.flow.collectLatest

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val productoRepository: ProductoRepository = ProductoRepository()
    private val descuentoRepository: DescuentoRepository = DescuentoRepository()
    private val usuarioRepository: UsuarioRepository


    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios.asStateFlow()
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val producto: StateFlow<List<Producto>> = _productos.asStateFlow()
    private val _descuentos = MutableStateFlow<List<Descuento>>(emptyList())
    val descuento: StateFlow<List<Descuento>> = _descuentos.asStateFlow()
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
        fetchDescuento()
        fetchUsuario()

        cargarUsuarioGuardado()
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