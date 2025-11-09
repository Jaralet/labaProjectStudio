package com.laba2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val imageEditText: EditText = findViewById(R.id.edit_text_image)
        val titleEditText: EditText = findViewById(R.id.edit_text_title)
        val descEditText: EditText = findViewById(R.id.edit_text_desc)
        val textEditText: EditText = findViewById(R.id.edit_text_text)
        val priceEditText: EditText = findViewById(R.id.edit_text_price)
        val saveButton: Button = findViewById(R.id.btn_save_product)

        saveButton.setOnClickListener {
            val image = imageEditText.text.toString().trim()
            val title = titleEditText.text.toString().trim()
            val desc = descEditText.text.toString().trim()
            val text = textEditText.text.toString().trim()
            val priceStr = priceEditText.text.toString().trim()

            if (image.isEmpty() || title.isEmpty() || desc.isEmpty() || text.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = priceStr.toIntOrNull()
            if (price == null || price <= 0) {
                Toast.makeText(this, "Введите корректную цену", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val product = Item(0, image, title, desc, text, price)
            val db = DbHelper(this, null)
            val success = db.addProduct(product)

            if (success) {
                Toast.makeText(this, "Товар добавлен", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Ошибка при добавлении товара", Toast.LENGTH_SHORT).show()
            }
        }
    }
}