package com.data.apexercise11

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.data.apexercise11.databinding.ActivityAddProductsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.data.apexercise11.DataProducts
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class AddProducts : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var binding: ActivityAddProductsBinding
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductsBinding.inflate(layoutInflater)
        fab = binding.floatingActionButton

        setContentView(binding.root)

        // Object instantiation
        databaseHelper = DatabaseHelper(this)

        binding.addBtn.setOnClickListener {
            val name = binding.txtName.text.toString()
            val desc = binding.txtDesc.text.toString()
            val price = binding.txtPrice.text.toString().toDouble()

            val image = getImageByteArray(binding.imageView2)

            if (name.isNotEmpty() && desc.isNotEmpty() && price != null && image != null) {
                val product = DataProducts(0, name, desc, price, image)
                addData(product)

                Toast.makeText(this, "New Product Added", Toast.LENGTH_SHORT).show()

                // Clear the input fields
                binding.txtName.text?.clear()
                binding.txtDesc.text?.clear()
                binding.txtPrice.text?.clear()
                binding.imageView2.setImageResource(android.R.color.transparent)

                // Refresh the displayed data
                getData()
            } else {
                Toast.makeText(this, "Please complete all required information", Toast.LENGTH_LONG).show()
            }
        }

        binding.floatingActionButton.setOnClickListener {
            openImagePicker()
        }

        binding.View4AllProducts.setOnClickListener{
            val intent = Intent(this, ViewProducts::class.java)

            startActivity(intent)
            Toast.makeText(this, "All Products", Toast.LENGTH_SHORT).show()
        }
    }


    val imagePickerActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val selectedImage: Uri? = data.data
                    // Process the selected image URI as needed
                    // For example, you can display the selected image in an ImageView
                    selectedImage?.let { uri ->
                        binding.imageView2.setImageURI(uri)
                    }
                }
            }
        }

    private fun getImageByteArray(imageView: ImageView): ByteArray? {
        val drawable = imageView.drawable as? BitmapDrawable
        val bitmap = drawable?.bitmap

        return bitmap?.let { bmp ->
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.toByteArray()
        }
    }


    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerActivityResult.launch(intent)
    }

    private fun getData(): MutableList<DataProducts> {
        return databaseHelper.getAllProducts()
    }

    private fun addData(product: DataProducts) {
        val imageByteArray = getImageByteArray(binding.imageView2)
        if (imageByteArray != null) {
            product.productImage = imageByteArray
        }

        databaseHelper.insertProduct(product)
        getData()
    }





}
