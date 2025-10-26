package com.example.ttmrepuestos.data.repository

import com.example.ttmrepuestos.data.local.Producto
import com.example.ttmrepuestos.data.local.ProductoDao
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val dao: ProductoDao) {
    val products: Flow<List<Producto>> = dao.getAllProducts()

    suspend fun insert(producto: Producto): Long = dao.insertProduct(producto)

    suspend fun update(producto: Producto) = dao.updateProduct(producto)

    suspend fun delete(producto: Producto) = dao.deleteProduct(producto)
}
