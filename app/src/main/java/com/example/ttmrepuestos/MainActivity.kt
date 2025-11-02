package com.example.ttmrepuestos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ttmrepuestos.data.local.AppDatabase
import com.example.ttmrepuestos.data.repository.ProductoRepository
import com.example.ttmrepuestos.data.repository.UsuarioRepository
import com.example.ttmrepuestos.ui.AppNavigation
import com.example.ttmrepuestos.ui.theme.TTMRepuestosTheme
import com.example.ttmrepuestos.viewmodel.ProductoViewModelFactory
import com.example.ttmrepuestos.viewmodel.UsuarioViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE products ADD COLUMN fotoUri TEXT")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `apellido` TEXT NOT NULL, `edad` INTEGER NOT NULL, `correo` TEXT NOT NULL, `telefono` TEXT NOT NULL, `contrasena` TEXT NOT NULL)")
            }
        }

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my_database"
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3).build()

        val productoRepo = ProductoRepository(db.productoDao())
        val productoFactory = ProductoViewModelFactory(productoRepo)

        val usuarioRepo = UsuarioRepository(db.usuarioDao())
        val usuarioFactory = UsuarioViewModelFactory(usuarioRepo)

        setContent {
            TTMRepuestosTheme {
                AppNavigation(
                    context = applicationContext,
                    productoFactory = productoFactory,
                    usuarioFactory = usuarioFactory
                )
            }
        }
    }
}
