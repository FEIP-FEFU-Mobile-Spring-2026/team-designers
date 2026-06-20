package com.feip.pinkpanther.repository

import android.content.Context
import com.feip.pinkpanther.models.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class ProductRepository(private val context: Context) {

    fun loadProducts(): List<Product> {
        return try {
            val jsonString = context.assets.open("products.json")
                .bufferedReader().use { it.readText() }

            val type = object : TypeToken<ProductResponse>() {}.type
            val response: ProductResponse = Gson().fromJson(jsonString, type)
            response.products ?: emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }

    private data class ProductResponse(
        val products: List<Product>? = null
    )
}