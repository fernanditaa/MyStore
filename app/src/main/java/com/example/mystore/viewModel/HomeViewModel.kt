package com.example.mystore.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class HomeViewModel(private val repository: ProductoRepository = ProductoRepository(),
                    private val descuentoRepository: DescuentoRepository = DescuentoRepository(),
                    private val usuarioRepository: UsuarioRepository = UsuarioRepository()
) : ViewModel() {


    private val _usuarios= MutableStateFlow<List<Usuario>>(emptyList())
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


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchProducto()
        fetchDescuento()
        fetchUsuario()
        registrarUsuario("Kat", "Hub", "kathub@kathub.cl", "kathub1234")
    }
    private fun fetchUsuario(){
        viewModelScope.launch {
            _isLoading.value= true
            try {
                _usuarios.value = usuarioRepository.obtenerUsuarios()
            }catch (e: Exception){
                print("Error al ingresar al usuario: ${e.localizedMessage}")
            }finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchDescuento(){
        viewModelScope.launch {
            _isLoading.value= true
            try {
                _descuentos.value = descuentoRepository.getDescuento()
            }catch (e: Exception){
                print("Error al cargar el descuento: ${e.localizedMessage}")
            }finally {
                _isLoading.value = false
            }
        }
    }
    private fun fetchProducto(){
        viewModelScope.launch {
            _isLoading.value = true
            try{
                _productos.value = repository.getProducto()
            }catch (e: Exception){
                println("Error al cargar el producto: ${e.localizedMessage}")
            }finally {
                _isLoading.value = false
            }
        }
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
    fun registrarUsuario(nombre: String, apellido: String, correo: String, contrasena:String): Boolean {
        val nuevoUsuario = Usuario(nombre, apellido, correo, contrasena, contrasena)
        val completado = usuarioRepository.registrarUsuario(nuevoUsuario)
        if (completado){
            _usuarios.value = _usuarios.value + nuevoUsuario
        }
        return completado
    }
    fun validarLogin(correo: String, contrasena: String): Boolean{
        val usuario = usuarioRepository.validarLogin(correo, contrasena)
        return usuario != null
    }
}