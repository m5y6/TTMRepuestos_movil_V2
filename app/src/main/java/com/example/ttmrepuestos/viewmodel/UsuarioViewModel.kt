package com.example.ttmrepuestos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ttmrepuestos.data.local.Usuario
import com.example.ttmrepuestos.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _loginResult = MutableStateFlow<Result<Usuario>?>(null)
    val loginResult: StateFlow<Result<Usuario>?> = _loginResult

    private val _registerResult = MutableStateFlow<Result<Unit>?>(null)
    val registerResult: StateFlow<Result<Unit>?> = _registerResult

    fun login(correo: String, contrasena: String) {
        viewModelScope.launch {
            try {
                val usuario = repository.iniciarSesion(correo, contrasena)
                if (usuario != null) {
                    _loginResult.value = Result.success(usuario)
                } else {
                    _loginResult.value = Result.failure(Exception("Correo o contraseña incorrectos"))
                }
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }

    fun register(usuario: Usuario) {
        viewModelScope.launch {
            try {
                repository.registrarUsuario(usuario)
                _registerResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
            }
        }
    }
}

class UsuarioViewModelFactory(private val repository: UsuarioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsuarioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
