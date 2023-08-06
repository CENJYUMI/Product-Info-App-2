package com.data.apexercise11


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.data.apexercise11.databinding.ProductLayoutBinding

class ProductAdapter (private val products: List<DataProducts>) : RecyclerView.Adapter<ProductViewHolder>() {

    var onDeleteClick: ((DataProducts) -> Unit)? = null
    var onUpdateClick: ((DataProducts) -> Unit)? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductLayoutBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, products [position].productName, Toast.LENGTH_SHORT).show()
            val image = products[position].productImage

            val intent = Intent(holder.itemView.context, ProductInfoActivity::class.java)
            if (image is ByteArray) {
                intent.putExtra("image", image)

            }
            intent.putExtra("productName", products[position].productName)
            intent.putExtra("productDesc", products[position].productDescription)
            intent.putExtra("price", products[position].price)
            holder.itemView.context.startActivities(arrayOf(intent))
        }

        holder.binding.apply {
            deleteBtn.setOnClickListener {
                onDeleteClick?.invoke(products[position])
            }
            updateBtn.setOnClickListener {
                onUpdateClick?.invoke(products[position])
            }
        }




    }

}
