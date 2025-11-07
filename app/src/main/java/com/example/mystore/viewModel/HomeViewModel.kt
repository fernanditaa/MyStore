package com.example.mystore.viewModel

import androidx.compose.runtime.State
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

class HomeViewModel(private val repository: ProductoRepository = ProductoRepository()) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val producto: StateFlow<List<Producto>> = _productos.asStateFlow()

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
}