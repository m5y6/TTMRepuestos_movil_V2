package com.example.ttmrepuestos.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ttmrepuestos.ui.login.LoginOrRegisterScreen
import com.example.ttmrepuestos.ui.login.LoginScreen
import com.example.ttmrepuestos.ui.login.RegisterScreen
import com.example.ttmrepuestos.ui.terminos.TerminosScreen
import com.example.ttmrepuestos.viewmodel.ProductoViewModelFactory
import com.example.ttmrepuestos.viewmodel.UsuarioViewModel
import com.example.ttmrepuestos.viewmodel.UsuarioViewModelFactory

@Composable
fun AppNavigation(
    context: Context,
    productoFactory: ProductoViewModelFactory,
    usuarioFactory: UsuarioViewModelFactory
) {
    val navController = rememberNavController()
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val terminosAceptados = sharedPreferences.getBoolean("terminos_aceptados", false)
    val startDestination = if (terminosAceptados) "login_or_register" else "terminos"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("terminos") {
            TerminosScreen(onContinueClicked = {
                sharedPreferences.edit().putBoolean("terminos_aceptados", true).apply()
                navController.navigate("login_or_register") {
                    popUpTo("terminos") { inclusive = true }
                }
            })
        }
        composable("login_or_register") {
            LoginOrRegisterScreen(
                onLoginClicked = { navController.navigate("login") },
                onRegisterClicked = { navController.navigate("register") }
            )
        }
        composable("login") {
            val viewModel: UsuarioViewModel = viewModel(factory = usuarioFactory)
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login_or_register") { inclusive = true }
                    }
                },
                onRegisterClicked = {
                    navController.navigate("register") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("register") {
            RegisterScreen(
                factory = usuarioFactory,
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true } // Vuelve al login despues de registrarse
                    }
                },
                onLoginClicked = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
        composable("main") {
            MainScreen(factory = productoFactory)
        }
    }
}
