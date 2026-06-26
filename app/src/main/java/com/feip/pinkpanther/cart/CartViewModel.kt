package com.feip.pinkpanther.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.feip.pinkpanther.models.CartItem
import com.feip.pinkpanther.models.Product
import com.feip.pinkpanther.models.Size
import com.feip.pinkpanther.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CartRepository(application)

    private val _cartItems = MutableStateFlow(repository.loadCart())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _showCheckoutMessage = MutableStateFlow(false)
    val showCheckoutMessage: StateFlow<Boolean> = _showCheckoutMessage.asStateFlow()

    private val _showClearDialog = MutableStateFlow(false)
    val showClearDialog: StateFlow<Boolean> = _showClearDialog.asStateFlow()

    fun addToCart(product: Product, size: Size?) {
        val sizeId = size?.id ?: ""
        val sizeName = size?.name ?: ""

        val currentItems = _cartItems.value.toMutableList()
        val existingIndex = currentItems.indexOfFirst {
            it.productId == product.id && it.sizeId == sizeId
        }

        if (existingIndex >= 0) {
            currentItems[existingIndex] = currentItems[existingIndex].copy(
                quantity = currentItems[existingIndex].quantity + 1
            )
        } else {
            currentItems.add(
                CartItem(
                    productId = product.id,
                    sizeId = sizeId,
                    sizeName = sizeName,
                    productName = product.name,
                    priceInKopecks = product.priceInKopecks,
                    imageUrl = product.imageUrl,
                    quantity = 1
                )
            )
        }
        _cartItems.value = currentItems
        repository.saveCart(currentItems)
    }

    fun increaseQuantity(index: Int) {
        val items = _cartItems.value.toMutableList()
        items[index] = items[index].copy(quantity = items[index].quantity + 1)
        _cartItems.value = items
        repository.saveCart(items)
    }

    fun decreaseQuantity(index: Int) {
        val items = _cartItems.value.toMutableList()
        if (items[index].quantity > 1) {
            items[index] = items[index].copy(quantity = items[index].quantity - 1)
            _cartItems.value = items
            repository.saveCart(items)
        }
    }

    fun removeItem(index: Int) {
        val items = _cartItems.value.toMutableList()
        items.removeAt(index)
        _cartItems.value = items
        repository.saveCart(items)
    }

    fun showClearDialog() {
        _showClearDialog.value = true
    }

    fun dismissClearDialog() {
        _showClearDialog.value = false
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        repository.clearCart()
        _showClearDialog.value = false
    }

    fun checkout() {
        _cartItems.value = emptyList()
        repository.clearCart()
        _showCheckoutMessage.value = true
    }

    fun dismissCheckoutMessage() {
        _showCheckoutMessage.value = false
    }

    fun getTotalPrice(): Int {
        return _cartItems.value.sumOf { it.priceInKopecks * it.quantity }
    }

    fun getItemCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }

    fun formatPrice(kopecks: Int): String {
        val rubles = kopecks / 100.0
        return String.format("%.2f ₽", rubles)
    }
}