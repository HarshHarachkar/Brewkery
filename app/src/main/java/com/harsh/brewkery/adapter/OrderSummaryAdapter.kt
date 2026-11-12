package com.harsh.brewkery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harsh.brewkery.data.model.CartItem
import com.harsh.brewkery.databinding.ItemOrderSummaryBinding

class OrderSummaryAdapter(
    private val items: List<CartItem>
) : RecyclerView.Adapter<OrderSummaryAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(
        val binding: ItemOrderSummaryBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderViewHolder {

        val binding = ItemOrderSummaryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OrderViewHolder,
        position: Int
    ) {

        val item = items[position]
        val binding = holder.binding

        binding.tvOrderProductName.text = item.name

        binding.tvOrderCustomizations.text =
            "${item.sizeName} • ${item.milkName} • ${item.sugarLevel}"

        binding.tvOrderQuantity.text =
            "×${item.quantity}"

        Glide.with(binding.root.context)
            .load(item.imageUrl)
            .centerCrop()
            .into(binding.ivOrderProduct)
    }

    override fun getItemCount(): Int = items.size
}