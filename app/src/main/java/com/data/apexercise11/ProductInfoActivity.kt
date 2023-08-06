package com.data.apexercise11

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.data.apexercise11.databinding.ActivityProductInfoBinding
import java.text.DecimalFormat



class ProductInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageByteArray = intent.getByteArrayExtra("image")
        if (imageByteArray != null) {
            val imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
            binding.Image.setImageBitmap(imageBitmap)
        } else {
            val image = intent.getStringExtra("image")
            if (image != null) {
                Glide.with(this)
                    .load(image)
                    .into(binding.Image)
            }
        }
        val productName = intent.getStringExtra("productName")
        val productDesc = intent.getStringExtra("productDesc")
        val price = intent.getDoubleExtra("price", 0.00)

        binding.txtName.text = productName
        binding.txtDesc.text = productDesc
        binding.txtPrice.text = formatPrice(price)

    }

    private fun formatPrice(price: Double): String {
        val formatter = DecimalFormat("#,##0.00", )
        return formatter.format(price)
    }
}
