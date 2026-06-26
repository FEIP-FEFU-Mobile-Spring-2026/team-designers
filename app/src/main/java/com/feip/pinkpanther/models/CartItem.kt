package com.feip.pinkpanther.models

data class CartItem(
    val productId: String,
    val sizeId: String,
    val sizeName: String,
    val productName: String,
    val priceInKopecks: Int,
    val imageUrl: String,
    var quantity: Int = 1
)