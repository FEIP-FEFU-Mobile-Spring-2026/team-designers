package com.feip.pinkpanther.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feip.pinkpanther.models.Product
import com.feip.pinkpanther.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            val products = repository.loadProducts()
            val categories = buildCategories(products)

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                products = products,
                categories = categories,
                selectedCategory = "Новинки"
            )
        }
    }

    private fun buildCategories(products: List<Product>): List<String> {
        val categories = products.map { it.category }.distinct().sorted()
        return listOf("Новинки") + categories
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun getFilteredProducts(): List<Product> {
        val state = _uiState.value
        return when (state.selectedCategory) {
            "Новинки" -> state.products.filter { it.tags?.contains("New") == true }
            else -> state.products.filter { it.category == state.selectedCategory }
        }
    }

    data class CatalogUiState(
        val isLoading: Boolean = true,
        val products: List<Product> = emptyList(),
        val categories: List<String> = emptyList(),
        val selectedCategory: String = ""
    )
}