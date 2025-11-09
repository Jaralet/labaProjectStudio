package com.laba2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ItemDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        val image: ImageView = findViewById(R.id.item_image_full)
        val title: TextView = findViewById(R.id.item_title_full)
        val text: TextView = findViewById(R.id.item_text_full)
        val price: TextView = findViewById(R.id.item_price_full)
        val addToCartButton: Button = findViewById(R.id.btn_add_to_cart)

        // Получаем данные из Intent
        val itemId = intent.getIntExtra("item_id", 0)
        val itemTitle = intent.getStringExtra("item_title") ?: "Название товара"
        val itemText = intent.getStringExtra("item_text") ?: "Описание товара"
        val itemPrice = intent.getIntExtra("item_price", 0)
        val imageName = intent.getStringExtra("item_image") ?: ""

        title.text = itemTitle
        text.text = itemText
        price.text = "${itemPrice}$"

        // Улучшенная загрузка изображения
        loadImage(imageName, image)

        addToCartButton.setOnClickListener {
            val db = DbHelper(this, null)
            val success = db.addToCart(itemId)
            if (success) {
                Toast.makeText(this, "Товар добавлен в корзину!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Ошибка при добавлении в корзину", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadImage(imagePath: String, imageView: ImageView) {
        try {
            if (imagePath.startsWith("http")) {
                Glide.with(this)
                    .load(imagePath)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(imageView)
            } else {
                val imageId = resources.getIdentifier(imagePath, "drawable", packageName)
                if (imageId != 0) {
                    imageView.setImageResource(imageId)
                } else {
                    imageView.setImageResource(R.drawable.ic_baseline_image_24)
                }
            }
        } catch (e: Exception) {
            Log.e("ItemDetailActivity", "Error loading image: ${e.message}")
            imageView.setImageResource(R.drawable.ic_baseline_error_24)
        }
    }
}