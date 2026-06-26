package com.feip.pinkpanther.repository

import android.content.Context
import android.content.SharedPreferences
import com.feip.pinkpanther.models.CartItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveCart(items: List<CartItem>) {
        val json = gson.toJson(items)
        prefs.edit().putString("cart_items", json).apply()
    }

    fun loadCart(): List<CartItem> {
        val json = prefs.getString("cart_items", null) ?: return emptyList()
        val type = object : TypeToken<List<CartItem>>() {}.type
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun clearCart() {
        prefs.edit().remove("cart_items").apply()
    }
}