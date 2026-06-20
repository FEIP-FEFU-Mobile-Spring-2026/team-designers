package com.feip.pinkpanther.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feip.pinkpanther.models.Product
import com.feip.pinkpanther.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            val products = repository.loadProducts()
            val categories = repository.getCategories(products)
            _uiState.value = CatalogUiState(
                products = products,
                categories = categories,
                selectedCategory = categories.firstOrNull() ?: "Новинки"
            )
        }
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun getCurrentProducts(): List<Product> {
        val state = _uiState.value
        return repository.getProductsByCategory(state.products, state.selectedCategory)
    }
}

data class CatalogUiState(
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String = "Новинки"
)