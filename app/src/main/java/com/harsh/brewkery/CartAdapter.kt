package com.harsh.brewkery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harsh.brewkery.data.model.CartItem
import com.harsh.brewkery.databinding.ItemCartBinding
import java.util.Locale

class CartAdapter(
    private val cartItems: MutableList<CartItem>,
    private val onCartChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(
        val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CartViewHolder,
        position: Int
    ) {
        val item = cartItems[position]
        val binding = holder.binding

        binding.tvCartProductName.text = item.name
        binding.tvCartCustomizations.text =
            "${item.sizeName} • ${item.milkName} • ${item.sugarLevel}"

        binding.tvCartItemPrice.text =
            String.format(Locale.US, "$%.2f", item.totalPrice)

        binding.tvCartQuantity.text = item.quantity.toString()

        Glide.with(binding.ivCartProduct.context)
            .load(item.imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_gallery)
            .centerCrop()
            .into(binding.ivCartProduct)

        binding.btnDecreaseQuantity.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity--
                notifyItemChanged(holder.bindingAdapterPosition)
                onCartChanged()
            }
        }

        binding.btnIncreaseQuantity.setOnClickListener {
            item.quantity++
            notifyItemChanged(holder.bindingAdapterPosition)
            onCartChanged()
        }

        binding.btnRemoveItem.setOnClickListener {
            val currentPosition = holder.bindingAdapterPosition

            if (currentPosition != RecyclerView.NO_POSITION) {
                cartItems.removeAt(currentPosition)
                notifyItemRemoved(currentPosition)
                onCartChanged()
            }
        }
    }

    override fun getItemCount(): Int = cartItems.size
}