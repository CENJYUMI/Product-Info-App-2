package com.data.apexercise11

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.data.apexercise11.databinding.ActivityViewProductsBinding
import com.data.apexercise11.databinding.DialogLayoutBinding

class ViewProducts : AppCompatActivity() {
    private lateinit var binding: ActivityViewProductsBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: MutableList<DataProducts>
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Object instantiation
        databaseHelper = DatabaseHelper(this)

        // Setup the RecyclerView
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Retrieve data from the database
        productList = getData()

        adapter = ProductAdapter(productList)


        adapter.onDeleteClick = { product ->
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Delete Product")
            alertDialogBuilder.setMessage("Are you sure you want to delete this product?")

            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                // Delete from the database
                delete(product.productId)
                // Remove from the list
                productList.remove(product)
                // Notify the adapter that data has changed
                adapter.notifyDataSetChanged()
            }

            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        adapter.onUpdateClick = { product ->
            val alertDialogBuilder = AlertDialog.Builder(this)

            val dialogLayout = layoutInflater.inflate(R.layout.dialog_layout, null)
            val dialogBinding = DialogLayoutBinding.bind(dialogLayout)
            alertDialogBuilder.setView(dialogLayout)

            dialogBinding.edtName.setText(product.productName)
            dialogBinding.edtDesc.setText(product.productDescription)
            dialogBinding.edtPrice.setText(String.format("%,.2f",product.price))


            // Use Glide to load the image into the ImageView
            Glide.with(this)
                .load(product.productImage)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(dialogBinding.imageView2)

            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                val productName = dialogBinding.edtName.text.toString()
                val productDesc = dialogBinding.edtDesc.text.toString()
                val productPrice = dialogBinding.edtPrice.text.toString().toDouble()
                val productImage = product.productImage

                val newProduct = DataProducts(product.productId, productName, productDesc, productPrice, productImage)
                update(newProduct)


                val updateNotePosition = productList.indexOfFirst { it.productId == product.productId }
                if (updateNotePosition != -1) {
                    productList[updateNotePosition] = newProduct
                    adapter.notifyItemChanged(updateNotePosition)
                }
            }

            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
        binding.addProducts.setOnClickListener {
            val intent = Intent(this, AddProducts::class.java)

            startActivity(intent)
            Toast.makeText(this, "Add Products", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
    }



    private fun getData(): MutableList<DataProducts> {
        return databaseHelper.getAllProducts()
    }

    private fun update(product: DataProducts) {
        databaseHelper.updateData(product)
        getData()
        Toast.makeText(applicationContext, "Product Updated!", Toast.LENGTH_SHORT).show()
    }

    private fun delete(id: Int) {
        databaseHelper.deleteData(id)
        getData()
        Toast.makeText(applicationContext, "Product Deleted!", Toast.LENGTH_SHORT).show()
    }
}
