package com.example.ttmrepuestos.ui.verproductos

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ttmrepuestos.R
import com.example.ttmrepuestos.data.local.Producto
import com.example.ttmrepuestos.ui.theme.CustomButton
import com.example.ttmrepuestos.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerProductosScreen(viewModel: ProductoViewModel) {
    val products by viewModel.products.collectAsState(initial = emptyList())
    val fotos by viewModel.fotosDeProductos.collectAsState()

    // Estados para los diálogos
    var showEditDialog by remember { mutableStateOf(false) }
    var productoToEdit by remember { mutableStateOf<Producto?>(null) }
    var newNombre by remember { mutableStateOf("") }
    var newPrecio by remember { mutableStateOf("") }
    var newDescripcion by remember { mutableStateOf("") }
    var newCategoria by remember { mutableStateOf("") }

    var showDetailsDialog by remember { mutableStateOf(false) }
    var productoToShow by remember { mutableStateOf<Producto?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.logo3),
            contentDescription = "Logo de fondo",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.15f),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            // --- ¡ELIMINADO! Se quita la TopAppBar ---
            containerColor = Color.Transparent 
        ) { innerPadding ->
            // Se usa un Column para añadir el título
            Column(modifier = Modifier.padding(innerPadding)) {
                Text(
                    text = "Listado de Productos",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(16.dp)
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(products) { producto ->
                        val foto = fotos[producto.id]
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    productoToShow = producto
                                    showDetailsDialog = true
                                }
                        ) {
                            ListItem(
                                headlineContent = { Text(producto.nombre) },
                                supportingContent = { Text("Precio: ${producto.precio} - Cat: ${producto.categoria}") },
                                leadingContent = {
                                    if (foto != null) {
                                        Image(bitmap = foto.asImageBitmap(), contentDescription = null, modifier = Modifier.size(56.dp))
                                    } else {
                                        Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(56.dp))
                                    }
                                },
                                trailingContent = {
                                    Row {
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
                                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Diálogo de edición
    if (showEditDialog && productoToEdit != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        productoToEdit?.let {
                            viewModel.updateProduct(it, newNombre, newPrecio.toIntOrNull() ?: 0, newDescripcion, newCategoria)
                        }
                        showEditDialog = false
                    },
                    colors = CustomButton.colors
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = { TextButton(onClick = { showEditDialog = false }) { Text("Cancelar") } },
            title = { Text("Editar producto") },
            text = {
                Column {
                    OutlinedTextField(value = newNombre, onValueChange = { newNombre = it }, label = { Text("Nombre") })
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = newPrecio, onValueChange = { newPrecio = it }, label = { Text("Precio") })
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = newDescripcion, onValueChange = { newDescripcion = it }, label = { Text("Descripción") })
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = newCategoria, onValueChange = { newCategoria = it }, label = { Text("Categoría") })
                }
            }
        )
    }

    // Diálogo de detalles
    if (showDetailsDialog && productoToShow != null) {
        val foto = fotos[productoToShow!!.id]
        ProductDetailsDialog(
            producto = productoToShow!!,
            foto = foto,
            onDismiss = { showDetailsDialog = false }
        )
    }
}

@Composable
fun ProductDetailsDialog(
    producto: Producto,
    foto: Bitmap?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = producto.nombre, style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (foto != null) {
                    Image(
                        bitmap = foto.asImageBitmap(),
                        contentDescription = "Foto del producto",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No tiene imagen", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text("Precio: ${producto.precio}", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                Text("Categoría: ${producto.categoria}", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Descripción: ${producto.descripcion}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = CustomButton.colors
            ) {
                Text("Cerrar")
            }
        }
    )
}
