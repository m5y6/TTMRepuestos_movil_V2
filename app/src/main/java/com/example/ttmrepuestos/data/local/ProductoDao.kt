package com.example.ttmrepuestos.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao
interface ProductoDao {
    @Query("SELECT * FROM products ORDER BY id DESC")
    fun getAllProducts(): Flow<List<Producto>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(producto: Producto)
    @Update
    suspend fun updateProduct(producto: Producto)
    @Delete
    suspend fun deleteProduct(producto: Producto)
}