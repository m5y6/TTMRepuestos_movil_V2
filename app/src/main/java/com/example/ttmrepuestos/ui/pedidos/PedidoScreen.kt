package com.example.ttmrepuestos.ui.pedidos

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.ttmrepuestos.data.local.Producto
import com.example.ttmrepuestos.viewmodel.ProductoViewModel

@Composable
fun PedidoScreen(navController: NavController, viewModel: ProductoViewModel) {
    WithPermission(permission = Manifest.permission.CAMERA) {
        ContenidoPedidoScreen(navController, viewModel)
    }
}

@Composable
fun ContenidoPedidoScreen(navController: NavController, viewModel: ProductoViewModel) {
    val productos by viewModel.products.collectAsState(initial = emptyList())
    var fotoCapturada by remember { mutableStateOf<Bitmap?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    // --- ¡CORREGIDO! Se obtiene el contexto fuera del remember ---
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            fotoCapturada = bitmap
            mostrarDialogo = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                PreviewView(it).apply {
                    this.controller = cameraController
                    cameraController.bindToLifecycle(lifecycleOwner)
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                onClick = { cameraLauncher.launch(null) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sacar Foto")
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
                Text("Volver a Inicio")
            }
        }
    }

    if (mostrarDialogo && fotoCapturada != null) {
        AsignarFotoDialog(
            productos = productos,
            onDismiss = { mostrarDialogo = false },
            onProductSelected = { producto ->
                viewModel.asignarFotoAProducto(producto.id, fotoCapturada!!)
                mostrarDialogo = false
            }
        )
    }
}

@Composable
fun AsignarFotoDialog(productos: List<Producto>, onDismiss: () -> Unit, onProductSelected: (Producto) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Asignar foto a un producto") },
        text = {
            LazyColumn {
                items(productos) { producto ->
                    ListItem(
                        headlineContent = { Text(producto.nombre) },
                        modifier = Modifier.clickable { onProductSelected(producto) }
                    )
                }
            }
        },
        confirmButton = { 
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

// --- CÓDIGO DE PERMISOS (sin cambios) ---
@Composable
fun PermissionRequiredScreen(
    modifier: Modifier = Modifier, 
    permission: String,
    onPermissionGranted: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            onPermissionGranted()
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        Button(
            modifier = modifier.align(Alignment.Center),
            onClick = { launcher.launch(permission) }
        ) {
            Text("Dar permiso a la camara")
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
        PermissionRequiredScreen(modifier = modifier, permission = permission) {
            permissionGranted = true
        }
    } else {
        content()
    }
}