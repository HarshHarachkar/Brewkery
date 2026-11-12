package com.harsh.brewkery.data.model

data class Order(
    val ticketId: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val deliveryFee: Double,
    val tax: Double,
    val total: Double,
    val status: String = "PREPARING",
    val createdAt: Long = System.currentTimeMillis()
)