package com.feip.pinkpanther.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.feip.pinkpanther.PinkPantherApplication
import com.feip.pinkpanther.repository.ProductRepository

@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel = viewModel(factory = CatalogViewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // ===== HEADER =====
        Text(
            text = "🐾 Розовая Пантера",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Divider(
            modifier = Modifier.padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )

        // ===== ТАБЫ КАТЕГОРИЙ =====
        TabRow(
            selectedTabIndex = maxOf(0, uiState.categories.indexOf(uiState.selectedCategory)),
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            uiState.categories.forEach { category ->
                Tab(
                    selected = category == uiState.selectedCategory,
                    onClick = { viewModel.selectCategory(category) },
                    text = {
                        Text(
                            text = category,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                )
            }
        }

        // ===== СПИСОК ТОВАРОВ =====
        val products = viewModel.getCurrentProducts()

        if (products.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Товары не найдены",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(products, key = { it.id }) { product ->
                    ProductCard(product = product)
                }
            }
        }
    }
}

// ===== FACTORY ДЛЯ VIEWMODEL =====
val CatalogViewModelFactory = viewModelFactory {
    initializer {
        val application = this[APPLICATION_KEY] as PinkPantherApplication
        val repository = ProductRepository(application)
        CatalogViewModel(repository)
    }
}