
package com.example.ttmrepuestos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ttmrepuestos.ui.home.HomeScreen
import com.example.ttmrepuestos.ui.pedidos.PedidoScreen
import com.example.ttmrepuestos.ui.products.ProductoScreen
import com.example.ttmrepuestos.viewmodel.ProductoViewModel
import com.example.ttmrepuestos.viewmodel.ProductoViewModelFactory

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Products : Screen("products")
    object Pedidos : Screen("pedidos")
}

@Composable
fun AppNavGraph(factory: ProductoViewModelFactory) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Products.route) {
            val viewModel: ProductoViewModel = viewModel(factory = factory)
            ProductoScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.Pedidos.route) {
            val viewModel: ProductoViewModel = viewModel(factory = factory)
            PedidoScreen(navController = navController, viewModel = viewModel)
        }
    }
}
