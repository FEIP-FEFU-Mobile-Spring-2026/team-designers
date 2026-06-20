package com.feip.pinkpanther.repository

import android.content.Context
import com.feip.pinkpanther.models.Product
import kotlinx.serialization.json.Json
import java.io.IOException

class ProductRepository(private val context: Context) {

    fun loadProducts(): List<Product> {
        return try {
            val jsonString = context.assets.open("products.json")
                .bufferedReader().use { it.readText() }

            val parsed = Json { ignoreUnknownKeys = true }
                .decodeFromString<ProductsResponse>(jsonString)
            parsed.products
        } catch (e: IOException) {
            emptyList()
        }
    }

    fun getCategories(products: List<Product>): List<String> {
        val categories = products.map { it.category }.distinct()
        return listOf("Новинки") + categories.filter { it != "Новинки" }
    }

    fun getProductsByCategory(products: List<Product>, category: String): List<Product> {
        return if (category == "Новинки") {
            products.filter { it.isNew }
        } else {
            products.filter { it.category == category }
        }
    }
}

@kotlinx.serialization.Serializable
private data class ProductsResponse(val products: List<Product>)