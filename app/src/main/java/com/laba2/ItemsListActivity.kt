package com.laba2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemsListActivity : AppCompatActivity() {

    private lateinit var itemsAdapter: ItemsAdapter
    private lateinit var db: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        db = DbHelper(this, null)
        val itemsRecyclerView: RecyclerView = findViewById(R.id.items_list_recycler)
        val cartButton: ImageButton = findViewById(R.id.btn_cart)

        val itemsList = db.getAllProducts()
        itemsAdapter = ItemsAdapter(itemsList, this)

        itemsRecyclerView.layoutManager = LinearLayoutManager(this)
        itemsRecyclerView.adapter = itemsAdapter

        cartButton.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Обновляем список при возвращении на экран
        val itemsList = db.getAllProducts()
        itemsAdapter.items = itemsList
        itemsAdapter.notifyDataSetChanged()
    }
}