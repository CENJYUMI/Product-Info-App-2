package com.data.apexercise11

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.data.apexercise11.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnView.setOnClickListener {
            val intent = Intent(this, ViewProducts::class.java)

            startActivity(intent)
            Toast.makeText(this, "All Products", Toast.LENGTH_SHORT).show()
        }
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddProducts::class.java)

            startActivity(intent)
            Toast.makeText(this, "Add Products", Toast.LENGTH_SHORT).show()
        }
    }
}