package com.harsh.brewkery

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.harsh.brewkery.adapter.OrderSummaryAdapter
import com.harsh.brewkery.data.order.OrderManager
import com.harsh.brewkery.databinding.ActivityOrderStatusBinding
import java.util.Locale

class OrderStatusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityOrderStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSystemBars()
        setupRecyclerView()
        setupButtons()
        loadOrderDetails()
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

        binding.rvOrderItems.apply {
            layoutManager = LinearLayoutManager(this@OrderStatusActivity)
            isNestedScrollingEnabled = false
        }
    }

    // BUTTON ACTIONS

    private fun setupButtons() {

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnBackToMenu.setOnClickListener {

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

        binding.btnViewDetails.setOnClickListener {

            val order = OrderManager.getCurrentOrder()

            if (order != null) {

                val message = """
                    Subtotal: ${formatPrice(order.subtotal)}

                    Delivery Fee: ${formatPrice(order.deliveryFee)}

                    Tax: ${formatPrice(order.tax)}

                    Total: ${formatPrice(order.total)}
                """.trimIndent()

                AlertDialog.Builder(this)
                    .setTitle("Order Price Details")
                    .setMessage(message)
                    .setPositiveButton("Close", null)
                    .show()
            }
        }
    }

    // LOAD ORDER DETAILS

    private fun loadOrderDetails() {

        val order = OrderManager.getCurrentOrder()

        if (order == null) {

            Toast.makeText(
                this,
                "Order information not found.",
                Toast.LENGTH_LONG
            ).show()

            finish()
            return
        }

        binding.tvTicketId.text = order.ticketId

        binding.tvOrderStatus.text = order.status

        binding.tvOrderItemCount.text =
            "${order.items.sumOf { it.quantity }} Items"

        binding.rvOrderItems.adapter =
            OrderSummaryAdapter(order.items)
    }

    // PRICE FORMATTING

    private fun formatPrice(price: Double): String {

        return String.format(
            Locale.US,
            "$%.2f",
            price
        )
    }
}