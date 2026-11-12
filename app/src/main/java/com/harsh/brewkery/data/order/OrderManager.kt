package com.harsh.brewkery.data.order

import com.harsh.brewkery.data.cart.CartManager
import com.harsh.brewkery.data.model.Order
import java.util.UUID

object OrderManager {

    private var currentOrder: Order? = null

    fun createOrder(): Order? {

        val cartItems = CartManager.getItems()

        if (cartItems.isEmpty()) {
            return null
        }

        // Create a separate snapshot of cart items.
        val orderItems = cartItems.map { it.copy() }

        // Generate a unique ticket ID.
        val ticketId = "BKY-${UUID.randomUUID()
            .toString()
            .take(8)
            .uppercase()}"

        val order = Order(
            ticketId = ticketId,
            items = orderItems,
            subtotal = CartManager.getSubtotal(),
            deliveryFee = CartManager.getDeliveryFee(),
            tax = CartManager.getTax(),
            total = CartManager.getTotal(),
            status = "PREPARING"
        )

        currentOrder = order

        // Clear cart only after successful order creation.
        CartManager.clearCart()

        return order
    }

    fun getCurrentOrder(): Order? {
        return currentOrder
    }
}