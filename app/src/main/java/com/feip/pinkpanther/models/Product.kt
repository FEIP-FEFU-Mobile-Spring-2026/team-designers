package com.feip.pinkpanther.models

data class Product(
    val id: Int,
    val name: String,
    val priceInKopecks: Int,
    val category: String,
    val imageUrl: String,  // Было image
    val tags: List<String> = emptyList(),
    val longDescription: String? = null
) {
    val isNew: Boolean
        get() = tags.contains("New")

    fun getDescription(): String = longDescription ?: "Описание пока не добавлено"
}