package com.example.ttmrepuestos.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ttmrepuestos.data.local.Usuario
import com.example.ttmrepuestos.viewmodel.UsuarioViewModel
import com.example.ttmrepuestos.viewmodel.UsuarioViewModelFactory

@Composable
fun RegisterScreen(
    factory: UsuarioViewModelFactory,
    onRegisterSuccess: () -> Unit,
    onLoginClicked: () -> Unit
) {
    val viewModel: UsuarioViewModel = viewModel(factory = factory)
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    val registerResult by viewModel.registerResult.collectAsState()

    LaunchedEffect(registerResult) {
        registerResult?.onSuccess {
            onRegisterSuccess()
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo Electrónico") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = contrasena, onValueChange = { contrasena = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val usuario = Usuario(
                        nombre = nombre,
                        apellido = apellido,
                        edad = edad.toIntOrNull() ?: 0,
                        correo = correo,
                        telefono = telefono,
                        contrasena = contrasena
                    )
                    viewModel.register(usuario)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onLoginClicked) {
                Text("¿Ya tienes cuenta? Inicia Sesión")
            }
        }
    }
}
