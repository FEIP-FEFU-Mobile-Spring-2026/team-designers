package com.feip.pinkpanther.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Catalog : Screen("catalog", "Каталог", Icons.Default.Home)
    object Cart : Screen("cart", "Корзина", Icons.Default.ShoppingCart)
}

val screens = listOf(Screen.Catalog, Screen.Cart)