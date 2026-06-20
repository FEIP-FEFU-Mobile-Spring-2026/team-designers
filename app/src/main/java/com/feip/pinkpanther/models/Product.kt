package com.feip.pinkpanther.models

data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val priceInKopecks: Long,
    val imageUrl: String,
    val tags: List<String>? = null
) {
    fun getPriceInRubles(): String {
        val rubles = priceInKopecks / 100
        val kopecks = priceInKopecks % 100
        return if (kopecks == 0L) {
            "$rubles ₽"
        } else {
            "$rubles.$kopecks ₽"
        }
    }
}