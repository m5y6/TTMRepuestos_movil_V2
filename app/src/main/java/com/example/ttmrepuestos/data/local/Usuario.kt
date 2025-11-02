package com.example.ttmrepuestos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val edad: Int,
    val correo: String,
    val telefono: String,
    val contrasena: String
)
