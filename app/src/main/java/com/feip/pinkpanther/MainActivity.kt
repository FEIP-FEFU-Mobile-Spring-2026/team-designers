package com.feip.pinkpanther

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.feip.pinkpanther.repository.ProductRepository
import com.feip.pinkpanther.navigation.Screen
import com.feip.pinkpanther.navigation.screens
import com.feip.pinkpanther.cart.CartScreen
import com.feip.pinkpanther.catalog.CatalogScreen
import com.feip.pinkpanther.catalog.CatalogViewModel

class MainActivity : ComponentActivity() {

    private val repository by lazy { ProductRepository(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PinkPantherApp()
            }
        }
    }

    @Composable
    fun PinkPantherApp() {
        val navController = rememberNavController()
        val catalogViewModel: CatalogViewModel = viewModel(
            factory = CatalogViewModelFactory(repository)
        )

        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFFFFE4E1),
                    tonalElevation = 8.dp
                ) {
                    screens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = navController.currentDestination?.route == screen.route,
                            onClick = {
                                navController.navigate(screen.route)
                            }
                        )
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.Catalog.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Screen.Catalog.route) {
                    CatalogScreen(viewModel = catalogViewModel)
                }
                composable(Screen.Cart.route) {
                    CartScreen()
                }
            }
        }
    }
}

class CatalogViewModelFactory(
    private val repository: ProductRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatalogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatalogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}