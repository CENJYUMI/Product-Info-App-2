package com.data.apexercise11

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "ryx_products.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
                CREATE TABLE product (
                product_id INTEGER PRIMARY KEY AUTOINCREMENT,
                product_name TEXT,
                product_description TEXT,
                product_price DOUBLE,
                product_image BLOB
                )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS product")
        onCreate(db)
    }

    // CREATE
    fun insertProduct(product: DataProducts) {
        val db = writableDatabase

        val sql = "INSERT INTO product (product_name, product_description, product_price, product_image) VALUES (?,?,?,?)"
        val args = arrayOf(product.productName, product.productDescription, product.price, product.productImage)
        db.execSQL(sql, args)
    }

    // READ
    fun getAllProducts(): MutableList<DataProducts> {
        val db = readableDatabase

        val cursor = db.rawQuery("SELECT * FROM product", null)
        val productList = mutableListOf<DataProducts>()

        while (cursor.moveToNext()) {
            val productId = cursor.getInt(0)
            val productName = cursor.getString(1)
            val productDescription = cursor.getString(2)
            val productPrice = cursor.getDouble(3)
            val productImage = cursor.getBlob(4)

            val newProduct = DataProducts(productId, productName, productDescription, productPrice, productImage)
            productList.add(newProduct)
        }

        cursor.close()
        return productList
    }

    // UPDATE
    fun updateData(product: DataProducts) {
        val db = writableDatabase

        val updateQuery = "UPDATE product SET product_name = '${product.productName}', product_description = '${product.productDescription}', product_price = ${String.format("%.2f", product.price)} WHERE product_id = ${product.productId};"
        db.execSQL(updateQuery)
    }

    // DELETE
    fun deleteData(productId: Int) {
        val db = writableDatabase
        val deleteQuery = "DELETE FROM product WHERE product_id = $productId"
        db.execSQL(deleteQuery)
    }
}
