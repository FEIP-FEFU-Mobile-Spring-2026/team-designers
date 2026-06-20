package com.feip.pinkpanther.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.feip.pinkpanther.cart.CartScreen
import com.feip.pinkpanther.catalog.CatalogScreen

sealed class Screen(val route: String, val title: String) {
    object Catalog : Screen("catalog", "Каталог")
    object Cart : Screen("cart", "Корзина")
}

@Composable
fun PinkPantherNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Catalog.route
    ) {
        composable(Screen.Catalog.route) { CatalogScreen() }
        composable(Screen.Cart.route) { CartScreen() }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val screens = listOf(Screen.Catalog, Screen.Cart)
    val navBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(initial = null)
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (screen == Screen.Catalog)
                            Icons.Default.ShoppingBag
                        else
                            Icons.Default.ShoppingCart,
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) }
            )
        }
    }
}