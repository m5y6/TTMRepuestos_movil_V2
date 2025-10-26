package com.example.ttmrepuestos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Producto::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
}