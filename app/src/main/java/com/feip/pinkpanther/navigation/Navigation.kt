package com.feip.pinkpanther.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.feip.pinkpanther.cart.CartScreen
import com.feip.pinkpanther.cart.CartViewModel
import com.feip.pinkpanther.catalog.CatalogScreen

sealed class Screen(val route: String, val title: String) {
    object Catalog : Screen("catalog", "Каталог")
    object Cart : Screen("cart", "Корзина")
}

@Composable
fun PinkPantherNavHost(
    navController: NavHostController,
    cartViewModel: CartViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Catalog.route
    ) {
        composable(Screen.Catalog.route) {
            CatalogScreen(cartViewModel = cartViewModel)
        }
        composable(Screen.Cart.route) {
            CartScreen(viewModel = cartViewModel)
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    cartViewModel: CartViewModel
) {
    val screens = listOf(Screen.Catalog, Screen.Cart)
    val navBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(initial = null)
    val currentRoute = navBackStackEntry?.destination?.route
    val cartItems by cartViewModel.cartItems.collectAsState()
    val itemCount = cartItems.sumOf { it.quantity }

    NavigationBar {
        screens.forEach { screen ->
            val isCart = screen == Screen.Cart
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
                    if (isCart && itemCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Color(0xFFFF69B4),
                                    contentColor = Color.White
                                ) {
                                    Text(text = "$itemCount")
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, screen.title)
                        }
                    } else {
                        Icon(
                            imageVector = if (screen == Screen.Catalog) Icons.Default.ShoppingBag else Icons.Default.ShoppingCart,
                            contentDescription = screen.title
                        )
                    }
                },
                label = { Text(screen.title) }
            )
        }
    }
}