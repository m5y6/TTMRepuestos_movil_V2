package com.example.ttmrepuestos.ui.products

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ttmrepuestos.data.local.Producto
import com.example.ttmrepuestos.viewmodel.ProductoViewModel

@Composable
fun ProductoScreen(viewModel: ProductoViewModel, navController: NavController, modifier: Modifier = Modifier) {
    val products by viewModel.products.collectAsState(initial = emptyList())
    val fotos by viewModel.fotosDeProductos.collectAsState()
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }

    // Estados para el modal de edición
    var showEditDialog by remember { mutableStateOf(false) }
    var productoToEdit by remember { mutableStateOf<Producto?>(null) }
    var newNombre by remember { mutableStateOf("") }
    var newPrecio by remember { mutableStateOf("") }
    var newDescripcion by remember { mutableStateOf("") }
    var newCategoria by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Gestión de Productos", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        // Formulario para agregar productos
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            TextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            TextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            TextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    if (nombre.isNotBlank()) {
                        viewModel.addProduct(nombre, precio.toIntOrNull() ?: 0, descripcion, categoria)
                        nombre = ""; precio = ""; descripcion = ""; categoria = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar Producto")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Lista de productos
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(products) { producto ->
                val foto = fotos[producto.id]
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // --- ¡NUEVO! Muestra la foto o un ícono ---
                    if (foto != null) {
                        Image(bitmap = foto.asImageBitmap(), contentDescription = null, modifier = Modifier.size(50.dp))
                    } else {
                        Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(50.dp))
                    }
                    Spacer(Modifier.width(16.dp))
                    Text("${producto.nombre} (${producto.precio})", modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        productoToEdit = producto
                        newNombre = producto.nombre
                        newPrecio = producto.precio.toString()
                        newDescripcion = producto.descripcion
                        newCategoria = producto.categoria
                        showEditDialog = true
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { viewModel.deleteProduct(producto) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
                Divider()
            }
        }

        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Volver a Inicio")
        }
    }

    // Modal de edición
    if (showEditDialog && productoToEdit != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    productoToEdit?.let {
                        viewModel.updateProduct(it, newNombre, newPrecio.toIntOrNull() ?: 0, newDescripcion, newCategoria)
                    }
                    showEditDialog = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = { TextButton(onClick = { showEditDialog = false }) { Text("Cancelar") } },
            title = { Text("Editar producto") },
            text = {
                Column {
                    TextField(value = newNombre, onValueChange = { newNombre = it }, label = { Text("Nombre") })
                    TextField(value = newPrecio, onValueChange = { newPrecio = it }, label = { Text("Precio") })
                    TextField(value = newDescripcion, onValueChange = { newDescripcion = it }, label = { Text("Descripción") })
                    TextField(value = newCategoria, onValueChange = { newCategoria = it }, label = { Text("Categoría") })
                }
            }
        )
    }
}