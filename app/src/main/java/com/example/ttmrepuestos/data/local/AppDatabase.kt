package com.example.ttmrepuestos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Producto::class, Usuario::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun usuarioDao(): UsuarioDao
}
