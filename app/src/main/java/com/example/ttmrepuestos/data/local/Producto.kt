package com.example.ttmrepuestos.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Producto (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val precio: Int,
    val descripcion: String,
    val categoria: String,
    val fotoUri: String? = null
)