package com.example.ttmrepuestos.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttmrepuestos.data.local.Producto
import com.example.ttmrepuestos.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductoViewModel(private val repository: ProductoRepository) : ViewModel() {
    val products = repository.products.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(),
        emptyList()
    )

    // --- ¡NUEVO! Almacén de fotos temporal ---
    private val _fotosDeProductos = MutableStateFlow<Map<Int, Bitmap>>(emptyMap())
    val fotosDeProductos = _fotosDeProductos.asStateFlow()

    fun asignarFotoAProducto(idProducto: Int, bitmap: Bitmap) {
        val nuevasFotos = _fotosDeProductos.value.toMutableMap()
        nuevasFotos[idProducto] = bitmap
        _fotosDeProductos.value = nuevasFotos
    }

    fun addProduct(nombre: String, precio: Int, descripcion: String, categoria: String) {
        viewModelScope.launch {
            repository.insert(Producto(nombre = nombre, precio = precio, descripcion = descripcion, categoria = categoria))
        }
    }

    fun deleteProduct(producto: Producto) {
        viewModelScope.launch {
            repository.delete(producto)
        }
    }

    fun updateProduct(producto: Producto, newNombre: String, newPrecio: Int, newDescripcion: String, newCategoria: String) {
        viewModelScope.launch {
            val updatedProducto = producto.copy(nombre = newNombre, precio = newPrecio, descripcion = newDescripcion, categoria = newCategoria)
            repository.update(updatedProducto)
        }
    }
}