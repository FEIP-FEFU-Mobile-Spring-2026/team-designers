package com.feip.pinkpanther.repository

import com.feip.pinkpanther.data.api.RetrofitClient
import com.feip.pinkpanther.models.Category
import com.feip.pinkpanther.models.Product

class ProductRepository {

    private val api = RetrofitClient.apiService
    private var categories: List<Category> = emptyList()
    private var allProducts: List<Product> = emptyList()

    suspend fun loadProducts(): Result<List<Product>> {
        return try {
            val response = api.getCatalog(RetrofitClient.TOKEN)
            categories = response.categories
            allProducts = response.items
            Result.success(response.items)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    fun getCategories(products: List<Product>): List<String> {
        val categoryNames = categories.map { it.name }
        return listOf("Новинки") + categoryNames.filter { it != "Новинки" }
    }

    fun getProductsByCategory(products: List<Product>, category: String): List<Product> {
        return if (category == "Новинки") {
            products.filter { it.isNew }
        } else {
            val categoryId = categories.find { it.name == category }?.id ?: ""
            products.filter { it.categoryId == categoryId }
        }
    }

    fun getCategoryName(categoryId: String): String {
        return categories.find { it.id == categoryId }?.name ?: ""
    }
}