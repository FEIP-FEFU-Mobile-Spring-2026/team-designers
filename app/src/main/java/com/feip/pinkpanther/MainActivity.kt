package com.feip.pinkpanther

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.feip.pinkpanther.cart.CartViewModel
import com.feip.pinkpanther.navigation.BottomNavigationBar
import com.feip.pinkpanther.navigation.PinkPantherNavHost
import com.feip.pinkpanther.ui.theme.PinkPantherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PinkPantherTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val cartViewModel: CartViewModel = viewModel()

                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.weight(1f)) {
                            PinkPantherNavHost(navController, cartViewModel)
                        }
                        BottomNavigationBar(navController, cartViewModel)
                    }
                }
            }
        }
    }
}