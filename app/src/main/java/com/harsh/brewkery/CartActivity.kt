package com.harsh.brewkery

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.harsh.brewkery.adapter.CartAdapter
import com.harsh.brewkery.data.cart.CartManager
import com.harsh.brewkery.data.order.OrderManager
import com.harsh.brewkery.databinding.ActivityCartBinding
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSystemBars()
        setupRecyclerView()
        setupButtons()

        refreshCart()
    }

    // SYSTEM BAR HANDLING

    private fun setupSystemBars() {

        WindowInsetsControllerCompat(
            window,
            window.decorView
        ).isAppearanceLightStatusBars = true

        val root = binding.root

        val originalLeft = root.paddingLeft
        val originalTop = root.paddingTop
        val originalRight = root.paddingRight
        val originalBottom = root.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                originalLeft + systemBars.left,
                originalTop + systemBars.top,
                originalRight + systemBars.right,
                originalBottom + systemBars.bottom
            )

            insets
        }

        ViewCompat.requestApplyInsets(root)
    }

    // RECYCLER VIEW

    private fun setupRecyclerView() {

        cartAdapter = CartAdapter(
            cartItems = CartManager.getItems()
        ) {
            refreshCart()
        }

        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = cartAdapter
            isNestedScrollingEnabled = false
        }
    }

    // BUTTON ACTIONS

    private fun setupButtons() {

        // Back Button
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Clear Cart Button
        binding.btnClearAll.setOnClickListener {

            if (CartManager.getItems().isNotEmpty()) {

                CartManager.clearCart()

                cartAdapter.notifyDataSetChanged()

                refreshCart()

                Toast.makeText(
                    this,
                    "Cart cleared successfully.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Explore Menu Button
        binding.btnExploreMenu.setOnClickListener {
            navigateToMenu()
        }

        // Promotional Banner
        binding.ivPromoBanner.setOnClickListener {
            navigateToMenu()
        }

        // Checkout Button
        binding.btnCheckout.setOnClickListener {

            if (CartManager.getItems().isEmpty()) {

                Toast.makeText(
                    this,
                    "Your cart is empty.",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            showCheckoutConfirmation()
        }
    }

    // NAVIGATE TO MENU

    private fun navigateToMenu() {

        val intent = Intent(
            this,
            MainActivity::class.java
        )

        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP

        startActivity(intent)
        finish()
    }

    // CHECKOUT CONFIRMATION

    private fun showCheckoutConfirmation() {

        val totalAmount = formatPrice(CartManager.getTotal())

        val dialog = AlertDialog.Builder(this)
            .setTitle("Confirm Your Order")
            .setMessage(
                "Your total is $totalAmount.\n\n" +
                        "Would you like to place your order?"
            )
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Place Order") { _, _ ->
                placeOrder()
            }
            .create()

        dialog.setOnShowListener {

            // Cream Dialog Background
            dialog.window?.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this,
                        R.color.cream
                    )
                )
            )

            // Title Colour
            dialog.findViewById<TextView>(
                androidx.appcompat.R.id.alertTitle
            )?.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.coffee_dark
                )
            )

            // Message Colour
            dialog.findViewById<TextView>(
                android.R.id.message
            )?.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.text_primary
                )
            )

            // Place Order Button Colour
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.caramel
                    )
                )

            // Cancel Button Colour
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.text_secondary
                    )
                )
        }

        dialog.show()
    }

    // PLACE ORDER

    private fun placeOrder() {

        val order = OrderManager.createOrder()

        if (order == null) {

            Toast.makeText(
                this,
                "Unable to place your order. Please try again.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        // Open Order Status Screen
        val intent = Intent(
            this,
            OrderStatusActivity::class.java
        )

        startActivity(intent)

        // Close Cart Screen
        finish()
    }

    // REFRESH CART

    private fun refreshCart() {

        val items = CartManager.getItems()
        val isEmpty = items.isEmpty()

        binding.rvCartItems.visibility =
            if (isEmpty) View.GONE else View.VISIBLE

        binding.tvCartItemCount.visibility =
            if (isEmpty) View.GONE else View.VISIBLE

        binding.layoutEmptyCart.visibility =
            if (isEmpty) View.VISIBLE else View.GONE

        binding.promoBanner.visibility =
            if (isEmpty) View.GONE else View.VISIBLE

        binding.cartBottomBar.visibility =
            if (isEmpty) View.GONE else View.VISIBLE

        binding.btnClearAll.visibility =
            if (isEmpty) View.GONE else View.VISIBLE

        binding.tvCartItemCount.text =
            "Your items (${CartManager.getItemCount()})"

        binding.tvOrderSummaryTitle.visibility =
            if (isEmpty) View.GONE else View.VISIBLE

        binding.orderSummaryCard.visibility =
            if (isEmpty) View.GONE else View.VISIBLE

        updatePriceSummary()

        cartAdapter.notifyDataSetChanged()
    }

    // PRICE SUMMARY

    private fun updatePriceSummary() {

        val subtotal = CartManager.getSubtotal()
        val deliveryFee = CartManager.getDeliveryFee()
        val tax = CartManager.getTax()
        val total = CartManager.getTotal()

        binding.tvSubtotal.text = formatPrice(subtotal)

        binding.tvDeliveryFee.text = formatPrice(deliveryFee)

        binding.tvTax.text = formatPrice(tax)

        binding.tvTotal.text = formatPrice(total)

        binding.tvCheckoutItemCount.text =
            "${CartManager.getItemCount()} Items"

        binding.tvCheckoutTotal.text =
            formatPrice(total)
    }

    // PRICE FORMATTING

    private fun formatPrice(price: Double): String {

        return String.format(
            Locale.US,
            "$%.2f",
            price
        )
    }

    // ACTIVITY LIFECYCLE

    override fun onResume() {
        super.onResume()

        if (::cartAdapter.isInitialized) {
            refreshCart()
        }
    }
}