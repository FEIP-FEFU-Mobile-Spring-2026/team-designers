package com.feip.pinkpanther

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.feip.pinkpanther.cart.CartScreen
import com.feip.pinkpanther.catalog.CatalogScreen

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Catalog : BottomNavItem("catalog", "Каталог", Icons.Default.Store)
    object Cart : BottomNavItem("cart", "Корзина", Icons.Default.ShoppingCart)
}

@Composable
fun MainScreen() {
    val navItems = listOf(BottomNavItem.Catalog, BottomNavItem.Cart)
    var selectedItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Catalog) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedItem == item,
                        onClick = { selectedItem = item }
                    )
                }
            }
        }
    ) { paddingValues ->
        when (selectedItem) {
            BottomNavItem.Catalog -> CatalogScreen()
            BottomNavItem.Cart -> CartScreen()
        }
    }
}