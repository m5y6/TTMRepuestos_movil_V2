package com.example.ttmrepuestos.ui.terminos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TerminosScreen(onContinueClicked: () -> Unit) {
    var isChecked by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black // Fondo negro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Términos y Condiciones",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Green, // Texto verde
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Al aceptar los terminos y condiciones de TTM Repuestos nosotros le daremos el acceso a nuestra aplicacion.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White, // Texto blanco para legibilidad
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Green,
                        uncheckedColor = Color.Green,
                        checkmarkColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("He leído y acepto los términos y condiciones", color = Color.White)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onContinueClicked,
                enabled = isChecked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.DarkGray,
                    disabledContentColor = Color.LightGray
                )
            ) {
                Text("Continuar")
            }
        }
    }
}
