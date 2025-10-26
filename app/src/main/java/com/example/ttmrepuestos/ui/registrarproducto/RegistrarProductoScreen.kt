package com.example.ttmrepuestos.ui.registrarproducto

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ttmrepuestos.R
import com.example.ttmrepuestos.ui.theme.CustomButton
import com.example.ttmrepuestos.viewmodel.ProductoViewModel
import kotlinx.coroutines.launch

@Composable
fun RegistrarProductoScreen(viewModel: ProductoViewModel) {
    WithPermission(permission = Manifest.permission.CAMERA) {
        ContenidoRegistrarProducto(viewModel = viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ContenidoRegistrarProducto(viewModel: ProductoViewModel) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fotoCapturada by remember { mutableStateOf<Bitmap?>(null) }

    val categorias = listOf("Filtros", "Frenos", "Suspension", "Motor", "Neumaticos", "Electrico")
    var categoriaSeleccionada by remember { mutableStateOf(categorias[0]) }
    var expanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            fotoCapturada = bitmap
        }
    }

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
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Registrar Nuevo Producto",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(16.dp)
                )
                Card(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // --- ¡NUEVO! Animación para la foto ---
                        AnimatedContent(targetState = fotoCapturada, label = "ImageAnimation") { targetBitmap ->
                            if (targetBitmap != null) {
                                Image(
                                    bitmap = targetBitmap.asImageBitmap(),
                                    contentDescription = "Foto del producto",
                                    modifier = Modifier.fillMaxWidth().height(200.dp)
                                )
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxWidth().height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Toca el botón \"Tomar Foto\" para añadir una imagen", textAlign = TextAlign.Center)
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = { cameraLauncher.launch(null) },
                            colors = CustomButton.colors
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                            Text(if (fotoCapturada == null) "Tomar Foto" else "Tomar Otra Foto")
                        }

                        Spacer(Modifier.height(16.dp))

                        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = categoriaSeleccionada,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Categoría") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                categorias.forEach { categoria ->
                                    DropdownMenuItem(
                                        text = { Text(categoria) },
                                        onClick = {
                                            categoriaSeleccionada = categoria
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                val canSave = nombre.isNotBlank() && precio.isNotBlank() && descripcion.isNotBlank() && fotoCapturada != null
                Button(
                    onClick = {
                        viewModel.addProduct(nombre, precio.toIntOrNull() ?: 0, descripcion, categoriaSeleccionada, fotoCapturada)
                        
                        nombre = ""
                        precio = ""
                        descripcion = ""
                        categoriaSeleccionada = categorias[0]
                        fotoCapturada = null
                        
                        scope.launch {
                            snackbarHostState.showSnackbar("¡Producto guardado con éxito!")
                        }
                    },
                    enabled = canSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        // --- ¡NUEVO! Animación de opacidad ---
                        .graphicsLayer { alpha = if (canSave) 1f else 0.6f },
                    colors = CustomButton.colors
                ) {
                    Text("Guardar Producto")
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

// --- El código de permisos no cambia ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionRequiredScreen(
    modifier: Modifier = Modifier,
    onPermissionGranted: () -> Unit,
    permission: String
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            onPermissionGranted()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Permiso Requerido") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "La cámara es necesaria para tomar fotos de los repuestos.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { launcher.launch(permission) },
                colors = CustomButton.colors
            ) {
                Text("Conceder Permiso")
            }
        }
    }
}

@Composable
fun WithPermission(
    modifier: Modifier = Modifier,
    permission: String,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    var permissionGranted by remember {
        mutableStateOf(context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
    }
    if (!permissionGranted) {
        PermissionRequiredScreen(modifier = modifier, onPermissionGranted = { permissionGranted = true }, permission = permission)
    } else {
        content()
    }
}
