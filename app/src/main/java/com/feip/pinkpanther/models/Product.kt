package com.feip.pinkpanther.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val category: String,
    val image: String,
    val tags: List<String> = emptyList()
) {
    val isNew: Boolean
        get() = tags.contains("New")
}