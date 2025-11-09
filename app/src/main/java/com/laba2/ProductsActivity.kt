package com.laba2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductsActivity : AppCompatActivity() {

    private lateinit var productAdapter: ProductAdminAdapter
    private lateinit var db: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        db = DbHelper(this, null)
        val productsRecyclerView: RecyclerView = findViewById(R.id.products_recycler_view)
        val addProductButton: FloatingActionButton = findViewById(R.id.btn_add_product)

        productAdapter = ProductAdminAdapter(ArrayList()) { productId ->
            db.deleteProduct(productId)
            Toast.makeText(this, "Товар удален", Toast.LENGTH_SHORT).show()
            loadProducts()
        }

        productsRecyclerView.layoutManager = LinearLayoutManager(this)
        productsRecyclerView.adapter = productAdapter

        addProductButton.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
    }

    private fun loadProducts() {
        val productList = db.getAllProducts()
        productAdapter.updateProducts(productList)
    }
}