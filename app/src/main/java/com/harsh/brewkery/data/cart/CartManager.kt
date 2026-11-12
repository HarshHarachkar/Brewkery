package com.harsh.brewkery.data.cart

import com.harsh.brewkery.data.model.CartItem

object CartManager {

    private val cartItems = mutableListOf<CartItem>()

    fun getItems(): MutableList<CartItem> {
        return cartItems
    }

    fun addItem(newItem: CartItem) {
        val existingItem = cartItems.find {
            it.productId == newItem.productId &&
                    it.sizeId == newItem.sizeId &&
                    it.milkId == newItem.milkId &&
                    it.sugarLevel == newItem.sugarLevel
        }

        if (existingItem != null) {
            existingItem.quantity += newItem.quantity
        } else {
            cartItems.add(newItem)
        }
    }

    fun removeItem(position: Int) {
        if (position in cartItems.indices) {
            cartItems.removeAt(position)
        }
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun getItemCount(): Int {
        return cartItems.sumOf { it.quantity }
    }

    fun getSubtotal(): Double {
        return cartItems.sumOf { it.totalPrice }
    }

    fun getTax(): Double {
        return getSubtotal() * 0.08
    }

    fun getDeliveryFee(): Double {
        return if (cartItems.isEmpty()) 0.0 else 2.50
    }

    fun getTotal(): Double {
        return getSubtotal() + getTax() + getDeliveryFee()
    }
}