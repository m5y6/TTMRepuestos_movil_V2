package com.example.ttmrepuestos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ttmrepuestos.data.local.AppDatabase
import com.example.ttmrepuestos.data.repository.ProductoRepository
import com.example.ttmrepuestos.ui.navigation.AppNavGraph
import com.example.ttmrepuestos.ui.theme.TTMRepuestosTheme
import com.example.ttmrepuestos.viewmodel.ProductoViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- ¡CORREGIDO! Implementada la migración manual ---
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE products ADD COLUMN fotoUri TEXT")
            }
        }

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my_database"
        ).addMigrations(MIGRATION_1_2).build()

        val repo = ProductoRepository(db.productoDao())
        val factory = ProductoViewModelFactory(repo)
        setContent {
            TTMRepuestosTheme {
                AppNavGraph(factory = factory)
            }
        }
    }
}