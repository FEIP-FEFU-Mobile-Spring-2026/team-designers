package com.feip.pinkpanther.models

data class Product(
    val id: String,
    val name: String,
    val shortDescription: String = "",
    val longDescription: String? = null,
    val priceInKopecks: Int,
    val imageUrl: String,
    val tags: List<String> = emptyList(),
    val categoryId: String,
    val sizes: List<Size> = emptyList(),
    val material: String = "",
    val weight: String = "",
    val season: String = "",
    val countryOfOrigin: String = ""
) {
    val isNew: Boolean
        get() = tags.any { it.equals("New", ignoreCase = true) }

    fun getDescription(): String = longDescription ?: shortDescription.ifEmpty { "Описание пока не добавлено" }
}

data class Size(
    val id: String,
    val name: String
)

data class Category(
    val id: String,
    val name: String
)

data class ProductsData(
    val categories: List<Category>,
    val items: List<Product>
)