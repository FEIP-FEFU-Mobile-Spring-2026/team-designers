package com.feip.pinkpanther.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feip.pinkpanther.models.Product
import com.feip.pinkpanther.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogViewModel : ViewModel() {

    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = repository.loadProducts()
            result.onSuccess { products ->
                val categories = repository.getCategories(products)
                _uiState.value = CatalogUiState(
                    products = products,
                    categories = categories,
                    selectedCategory = categories.firstOrNull() ?: "Новинки",
                    isLoading = false
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Ошибка загрузки"
                )
            }
        }
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun getCurrentProducts(): List<Product> {
        val state = _uiState.value
        return repository.getProductsByCategory(state.products, state.selectedCategory)
    }

    fun getCategoryName(categoryId: String): String {
        return repository.getCategoryName(categoryId)
    }

    fun formatPrice(kopecks: Int): String {
        val rubles = kopecks / 100.0
        return String.format("%.2f ₽", rubles)
    }
}

data class CatalogUiState(
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String = "Новинки",
    val isLoading: Boolean = false,
    val error: String? = null
)