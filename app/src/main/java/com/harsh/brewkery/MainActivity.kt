package com.harsh.brewkery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.harsh.brewkery.adapter.ProductAdapter
import com.harsh.brewkery.data.cart.CartManager
import com.harsh.brewkery.data.model.Product
import com.harsh.brewkery.data.repository.BrewkeryRepository
import com.harsh.brewkery.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val repository = BrewkeryRepository()

    private var allProducts: List<Product> = emptyList()

    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        ThemeManager.applySavedTheme(this)

        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSystemBars()
        setupRecyclerView()
        setupCategoryFilters()
        setupHomeActions()

        fetchMenu()
    }

    // SYSTEM BAR HANDLING

    private fun setupSystemBars() {

        val isDarkMode = ThemeManager.isDarkMode(this)

        WindowInsetsControllerCompat(
            window,
            window.decorView
        ).isAppearanceLightStatusBars = !isDarkMode

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

        productAdapter = ProductAdapter(emptyList()) { product ->

            val intent = Intent(
                this,
                ProductDetailsActivity::class.java
            )

            intent.putExtra(
                ProductDetailsActivity.EXTRA_PRODUCT_ID,
                product.id
            )

            startActivity(intent)
        }

        binding.rvProducts.layoutManager =
            LinearLayoutManager(this)

        binding.rvProducts.adapter = productAdapter
    }

    // HOME SCREEN ACTIONS

    private fun setupHomeActions() {
        binding.btnCart.setOnClickListener { openCart() }

        binding.tvCheckout.setOnClickListener { openCart() }

        binding.tvViewAll.setOnClickListener {
            filterProducts(null)
            binding.rvProducts.scrollToPosition(0)
        }

        // Light / Dark Theme Toggle
        binding.btnTheme.setOnClickListener {
            ThemeManager.toggleTheme(this)
        }

        updateCartSummary()
    }

    // OPEN CART

    private fun openCart() {

        startActivity(
            Intent(
                this,
                CartActivity::class.java
            )
        )
    }

    // UPDATE CART SUMMARY

    private fun updateCartSummary() {

        val itemCount = CartManager.getItemCount()
        val total = CartManager.getTotal()

        // Header Cart Badge
        binding.tvHeaderCartCount.text = itemCount.toString()

        binding.tvHeaderCartCount.visibility =
            if (itemCount > 0) View.VISIBLE else View.GONE

        // Bottom Cart Item Count
        binding.tvCartCount.text = itemCount.toString()

        // Bottom Cart Total
        binding.tvCartTotal.text = formatPrice(total)

        // Bottom Cart Bar Visibility
        binding.cartSummary.visibility =
            if (itemCount > 0) View.VISIBLE else View.GONE
    }

    // PRICE FORMATTING

    private fun formatPrice(price: Double): String {

        return String.format(
            Locale.US,
            "$%.2f",
            price
        )
    }

    // FETCH MENU FROM API

    private fun fetchMenu() {

        lifecycleScope.launch {

            try {

                val menu = withContext(Dispatchers.IO) {
                    repository.getMenu()
                }

                allProducts = menu.items

                Log.d(
                    "BREWKERY",
                    "Products loaded: ${allProducts.size}"
                )

                productAdapter.updateProducts(allProducts)

            } catch (e: Exception) {

                Log.e(
                    "BREWKERY",
                    "API Error",
                    e
                )

                Toast.makeText(
                    this@MainActivity,
                    "Unable to load menu. Check your internet.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // CATEGORY FILTERS

    private fun setupCategoryFilters() {

        binding.tvAllCategory.setOnClickListener {
            filterProducts(null)
        }

        binding.tvHotCoffee.setOnClickListener {
            filterProducts("cat_hot_coffee")
        }

        binding.tvColdBrews.setOnClickListener {
            filterProducts("cat_cold_brews")
        }

        binding.tvBakery.setOnClickListener {
            filterProducts("cat_bakery")
        }

        binding.tvDesserts.setOnClickListener {

            Toast.makeText(
                this,
                "Desserts are not available yet.",
                Toast.LENGTH_SHORT
            ).show()
        }
        // Set ALL category as selected when Home opens
        updateSelectedCategory(null)
    }

    // FILTER PRODUCTS

    private fun filterProducts(categoryId: String?) {

        val filteredProducts = if (categoryId == null) {

            allProducts

        } else {

            allProducts.filter {
                it.categoryId == categoryId
            }
        }

        productAdapter.updateProducts(filteredProducts)

        updateSelectedCategory(categoryId)

        Log.d(
            "BREWKERY",
            "Filtered products: ${filteredProducts.size}"
        )
    }

    // UPDATE SELECTED CATEGORY DESIGN

    private fun updateSelectedCategory(categoryId: String?) {

        // ALL
        binding.tvAllCategory.setBackgroundResource(
            if (categoryId == null)
                R.drawable.bg_category_selected
            else
                R.drawable.bg_category_unselected
        )

        (binding.tvAllCategory.getChildAt(1) as android.widget.TextView)
            .setTextColor(
                getColor(
                    if (categoryId == null)
                        android.R.color.white
                    else
                        R.color.coffee_dark
                )
            )

        // HOT COFFEE
        binding.tvHotCoffee.setBackgroundResource(
            if (categoryId == "cat_hot_coffee")
                R.drawable.bg_category_selected
            else
                R.drawable.bg_category_unselected
        )

        (binding.tvHotCoffee.getChildAt(1) as android.widget.TextView)
            .setTextColor(
                getColor(
                    if (categoryId == "cat_hot_coffee")
                        android.R.color.white
                    else
                        R.color.coffee_dark
                )
            )

        // COLD BREWS
        binding.tvColdBrews.setBackgroundResource(
            if (categoryId == "cat_cold_brews")
                R.drawable.bg_category_selected
            else
                R.drawable.bg_category_unselected
        )

        (binding.tvColdBrews.getChildAt(1) as android.widget.TextView)
            .setTextColor(
                getColor(
                    if (categoryId == "cat_cold_brews")
                        android.R.color.white
                    else
                        R.color.coffee_dark
                )
            )

        // BAKERY
        binding.tvBakery.setBackgroundResource(
            if (categoryId == "cat_bakery")
                R.drawable.bg_category_selected
            else
                R.drawable.bg_category_unselected
        )

        (binding.tvBakery.getChildAt(1) as android.widget.TextView)
            .setTextColor(
                getColor(
                    if (categoryId == "cat_bakery")
                        android.R.color.white
                    else
                        R.color.coffee_dark
                )
            )
    }
    // REFRESH WHEN RETURNING TO HOME

    override fun onResume() {
        super.onResume()

        if (::binding.isInitialized) {
            updateCartSummary()
        }
    }
}