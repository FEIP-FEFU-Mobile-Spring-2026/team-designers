package com.feip.pinkpanther.repository

import android.content.Context
import com.feip.pinkpanther.models.Category
import com.feip.pinkpanther.models.Product
import com.feip.pinkpanther.models.ProductsData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class ProductRepository(private val context: Context) {

    private var categories: List<Category> = emptyList()
    private var allProducts: List<Product> = emptyList()

    fun loadProducts(): List<Product> {
        return try {
            val jsonString = context.assets.open("products.json")
                .bufferedReader().use { it.readText() }

            val type = object : TypeToken<ProductsData>() {}.type
            val data: ProductsData = Gson().fromJson(jsonString, type)
            categories = data.categories
            allProducts = data.items
            data.items
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
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
}