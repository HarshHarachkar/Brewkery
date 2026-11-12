package com.harsh.brewkery.data.model

data class CartItem(
    val productId: Int,
    val name: String,
    val imageUrl: String,
    val basePrice: Double,
    val sizeId: String,
    val sizeName: String,
    val sizeExtraPrice: Double,
    val milkId: String,
    val milkName: String,
    val milkExtraPrice: Double,
    val sugarLevel: String,
    var quantity: Int = 1
) {
    val unitPrice: Double
        get() = basePrice + sizeExtraPrice + milkExtraPrice

    val totalPrice: Double
        get() = unitPrice * quantity
}