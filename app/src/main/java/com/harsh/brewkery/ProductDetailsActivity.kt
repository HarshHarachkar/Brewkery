package com.harsh.brewkery

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.harsh.brewkery.data.cart.CartManager
import com.harsh.brewkery.data.model.CartItem
import com.harsh.brewkery.data.model.ItemDetails
import com.harsh.brewkery.data.model.MilkOption
import com.harsh.brewkery.data.model.SizeOption
import com.harsh.brewkery.data.repository.BrewkeryRepository
import com.harsh.brewkery.databinding.ActivityProductDetailsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding

    private val repository = BrewkeryRepository()

    private var product: ItemDetails? = null

    private var productId: Int = -1
    private var quantity = 1

    private var selectedSize: SizeOption? = null
    private var selectedMilk: MilkOption? = null
    private var selectedSugar: String? = null

    private val coffeeDark = Color.rgb(56, 37, 30)
    private val caramel = Color.rgb(201, 93, 58)
    private val cream = Color.rgb(250, 247, 242)
    private val divider = Color.rgb(235, 224, 214)
    private val white = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSystemBars()
        setupButtons()
        updateCartCount()

        productId = intent.getIntExtra(EXTRA_PRODUCT_ID, -1)

        if (productId != -1) {
            fetchProductDetails()
        } else {
            Toast.makeText(
                this,
                "Product ID not found.",
                Toast.LENGTH_LONG
            ).show()

            finish()
        }
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


    // BUTTON ACTIONS
    private fun setupButtons() {

        binding.btnDetailsBack.setOnClickListener {
            finish()
        }

        binding.btnQuantityMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updatePrice()
            }
        }

        binding.btnQuantityPlus.setOnClickListener {
            quantity++
            updatePrice()
        }

        binding.btnAddToCart.setOnClickListener {
            addProductToCart()
        }

        binding.btnDetailsCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    // UPDATE CART BADGE COUNT
    private fun updateCartCount() {

        val count = CartManager.getItemCount()

        binding.tvDetailsCartCount.text = count.toString()

        binding.tvDetailsCartCount.visibility =
            if (count > 0) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }
    }

    // ADD PRODUCT TO CART

    private fun addProductToCart() {

        val currentProduct = product ?: return

        val size = selectedSize
        val milk = selectedMilk
        val sugar = selectedSugar

        if (size == null || milk == null || sugar == null) {

            Toast.makeText(
                this,
                "Please select all customizations.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val cartItem = CartItem(
            productId = currentProduct.id,
            name = currentProduct.name,
            imageUrl = currentProduct.imageUrl,
            basePrice = currentProduct.basePrice,

            sizeId = size.id,
            sizeName = size.label,
            sizeExtraPrice = size.extraPrice,

            milkId = milk.id,
            milkName = milk.name,
            milkExtraPrice = milk.extraPrice,

            sugarLevel = sugar,
            quantity = quantity
        )

        CartManager.addItem(cartItem)
        updateCartCount()

        Toast.makeText(
            this,
            "${currentProduct.name} added to cart!",
            Toast.LENGTH_SHORT
        ).show()

        val intent = Intent(
            this,
            CartActivity::class.java
        )

        startActivity(intent)
    }

    // FETCH PRODUCT FROM API

    private fun fetchProductDetails() {

        lifecycleScope.launch {

            try {

                val result = withContext(Dispatchers.IO) {
                    repository.getItemDetails(productId)
                }

                product = result

                Log.d(
                    "BREWKERY_DETAILS",
                    "Loaded product: ${result.name}"
                )

                displayProductDetails(result)
                setupCustomizationOptions(result)

            } catch (e: Exception) {

                Log.e(
                    "BREWKERY_DETAILS",
                    "Product API error",
                    e
                )

                Toast.makeText(
                    this@ProductDetailsActivity,
                    "Unable to load product details.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // DISPLAY PRODUCT INFORMATION

    private fun displayProductDetails(product: ItemDetails) {

        binding.tvDetailName.text = product.name

        binding.tvDetailPrice.text =
            formatPrice(product.basePrice)

        binding.tvDetailRating.text =
            "⭐ ${product.rating} (${product.reviewCount} reviews)"

        binding.tvDetailDescription.text =
            product.description

        binding.tvDetailBadge.text =
            product.badge ?: ""

        binding.tvIngredients.text =
            product.ingredients.joinToString(" • ")

        Glide.with(this)
            .load(product.imageUrl)
            .centerCrop()
            .into(binding.ivDetailProduct)

        updatePrice()
    }

    // LOAD CUSTOMIZATION OPTIONS FROM API

    private fun setupCustomizationOptions(product: ItemDetails) {

        val customizations = product.customizations

        // SIZE OPTIONS

        selectedSize = customizations.sizes.firstOrNull()

        binding.sizeOptionsContainer.removeAllViews()

        customizations.sizes.forEach { size ->

            val optionView = createOptionView(
                title = size.label,
                subtitle = formatExtraPrice(size.extraPrice),
                isSelected = size == selectedSize
            )

            optionView.setOnClickListener {

                selectedSize = size

                refreshCustomizationOptions(product)
                updatePrice()
            }

            binding.sizeOptionsContainer.addView(optionView)
        }

        // MILK OPTIONS

        selectedMilk = customizations.milkOptions.firstOrNull()

        binding.milkOptionsContainer.removeAllViews()

        customizations.milkOptions.forEach { milk ->

            val optionView = createOptionView(
                title = milk.name,
                subtitle = formatExtraPrice(milk.extraPrice),
                isSelected = milk == selectedMilk
            )

            optionView.setOnClickListener {

                selectedMilk = milk

                refreshCustomizationOptions(product)
                updatePrice()
            }

            binding.milkOptionsContainer.addView(optionView)
        }

        // SUGAR OPTIONS

        selectedSugar = customizations.sugarLevels.firstOrNull()

        binding.sugarOptionsContainer.removeAllViews()

        customizations.sugarLevels.forEach { sugar ->

            val optionView = createOptionView(
                title = sugar,
                subtitle = "",
                isSelected = sugar == selectedSugar
            )

            optionView.setOnClickListener {

                selectedSugar = sugar

                refreshCustomizationOptions(product)
                updatePrice()
            }

            binding.sugarOptionsContainer.addView(optionView)
        }

        updatePrice()
    }

    // CREATE CUSTOMIZATION OPTION VIEW

    private fun createOptionView(
        title: String,
        subtitle: String,
        isSelected: Boolean
    ): TextView {

        val textView = TextView(this)

        textView.text = if (subtitle.isNotEmpty()) {
            "$title\n$subtitle"
        } else {
            title
        }

        textView.gravity = Gravity.CENTER
        textView.textSize = 12f
        textView.maxLines = 4
        textView.setLineSpacing(2.dp.toFloat(), 1f)
        textView.setPadding(8.dp, 10.dp, 8.dp, 10.dp)

        textView.background = createOptionBackground(isSelected)

        textView.setTextColor(
            if (isSelected) white else coffeeDark
        )

        val params = android.widget.LinearLayout.LayoutParams(
            145.dp,
            88.dp
        )

        params.marginEnd = 8.dp
        textView.layoutParams = params

        return textView
    }

    // OPTION BACKGROUND

    private fun createOptionBackground(
        isSelected: Boolean
    ): GradientDrawable {

        return GradientDrawable().apply {

            cornerRadius = 18.dp.toFloat()

            setColor(
                if (isSelected) coffeeDark else cream
            )

            setStroke(
                if (isSelected) 2.dp else 1.dp,
                if (isSelected) caramel else divider
            )
        }
    }

    // REFRESH SELECTED OPTIONS

    private fun refreshCustomizationOptions(product: ItemDetails) {

        for (index in 0 until binding.sizeOptionsContainer.childCount) {

            val size = product.customizations.sizes[index]

            binding.sizeOptionsContainer
                .getChildAt(index)
                .apply {

                    background = createOptionBackground(
                        size == selectedSize
                    )

                    (this as TextView).setTextColor(
                        if (size == selectedSize) white else coffeeDark
                    )
                }
        }

        for (index in 0 until binding.milkOptionsContainer.childCount) {

            val milk = product.customizations.milkOptions[index]

            binding.milkOptionsContainer
                .getChildAt(index)
                .apply {

                    background = createOptionBackground(
                        milk == selectedMilk
                    )

                    (this as TextView).setTextColor(
                        if (milk == selectedMilk) white else coffeeDark
                    )
                }
        }

        for (index in 0 until binding.sugarOptionsContainer.childCount) {

            val sugar = product.customizations.sugarLevels[index]

            binding.sugarOptionsContainer
                .getChildAt(index)
                .apply {

                    background = createOptionBackground(
                        sugar == selectedSugar
                    )

                    (this as TextView).setTextColor(
                        if (sugar == selectedSugar) white else coffeeDark
                    )
                }
        }
    }

    // DYNAMIC PRICE CALCULATION

    private fun updatePrice() {

        val currentProduct = product ?: return

        val sizePrice = selectedSize?.extraPrice ?: 0.0
        val milkPrice = selectedMilk?.extraPrice ?: 0.0

        val unitPrice =
            currentProduct.basePrice + sizePrice + milkPrice

        val totalPrice = unitPrice * quantity

        binding.tvDetailPrice.text = formatPrice(unitPrice)

        binding.tvQuantity.text = quantity.toString()

        binding.btnAddToCart.text =
            "Add to Cart  •  ${formatPrice(totalPrice)}"
    }

    // PRICE FORMATTING

    private fun formatPrice(price: Double): String {

        return String.format(
            Locale.US,
            "$%.2f",
            price
        )
    }

    private fun formatExtraPrice(price: Double): String {

        return if (price == 0.0) {
            "+$0.00"
        } else {
            "+${formatPrice(price)}"
        }
    }

    // DP CONVERSION

    private val Int.dp: Int
        get() = (this * resources.displayMetrics.density).toInt()

    // REFRESH CART COUNT WHEN RETURNING TO DETAILS
    override fun onResume() {
        super.onResume()
        updateCartCount()
    }
    companion object {
        const val EXTRA_PRODUCT_ID = "extra_product_id"
    }
}