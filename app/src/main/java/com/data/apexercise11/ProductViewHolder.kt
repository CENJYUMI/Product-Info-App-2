package com.data.apexercise11

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.data.apexercise11.databinding.ProductLayoutBinding

class ProductViewHolder (val binding: ProductLayoutBinding):RecyclerView.ViewHolder(binding.root) {

    fun bind (product:DataProducts){
        binding.productName.text = product.productName
        binding.productDesc.text = product.productDescription
        binding.price.text = String.format("%,.2f", product.price)
        Glide.with(itemView.context)
            .load(product.productImage)
            .into(binding.image)


    }
}