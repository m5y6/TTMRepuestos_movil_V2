package com.example.ttmrepuestos.data.repository

import com.example.ttmrepuestos.data.local.Usuario
import com.example.ttmrepuestos.data.local.UsuarioDao

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    suspend fun registrarUsuario(usuario: Usuario) {
        usuarioDao.insertUser(usuario)
    }

    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {
        val usuario = usuarioDao.getUserByEmail(correo)
        if (usuario != null && usuario.contrasena == contrasena) {
            return usuario
        }
        return null
    }
}
