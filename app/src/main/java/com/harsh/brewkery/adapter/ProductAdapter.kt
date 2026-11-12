package com.harsh.brewkery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harsh.brewkery.data.model.Product
import com.harsh.brewkery.databinding.ItemProductBinding

class ProductAdapter(
    private var products: List<Product>,
    private val onCustomizeClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(
        val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {

        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {

        val product = products[position]
        val binding = holder.binding

        binding.tvProductName.text = product.name
        binding.tvBadge.text = product.badge ?: ""

        binding.tvRating.text =
            "⭐ ${product.rating} (${product.reviewCount})"

        binding.tvPrice.text =
            "$${String.format("%.2f", product.basePrice)}"

        Glide.with(binding.ivProduct.context)
            .load(product.imageUrl)
            .centerCrop()
            .into(binding.ivProduct)

        binding.btnCustomize.setOnClickListener {
            onCustomizeClick(product)
        }

        binding.root.setOnClickListener {
            onCustomizeClick(product)
        }
    }

    override fun getItemCount(): Int = products.size

    // Update products when a category is selected
    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}