package com.laba2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    private lateinit var cartAdapter: CartAdapter
    private lateinit var db: DbHelper
    private lateinit var totalPriceText: TextView
    private lateinit var emptyCartText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        db = DbHelper(this, null)
        val cartRecyclerView: RecyclerView = findViewById(R.id.cart_recycler_view)
        val checkoutButton: Button = findViewById(R.id.btn_checkout)
        totalPriceText = findViewById(R.id.total_price_text)
        emptyCartText = findViewById(R.id.empty_cart_text)

        cartAdapter = CartAdapter(
            ArrayList(),
            onRemoveClick = { cartItemId ->
                db.removeFromCart(cartItemId)
                loadCartItems()
                Toast.makeText(this, "Товар удален из корзины", Toast.LENGTH_SHORT).show()
            },
            onQuantityChange = { cartItemId, newQuantity ->
                db.updateCartItemQuantity(cartItemId, newQuantity)
                loadCartItems()
            }
        )

        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartRecyclerView.adapter = cartAdapter

        checkoutButton.setOnClickListener {
            val cartItems = db.getCartItems()
            if (cartItems.isNotEmpty()) {
                val totalPrice = db.getTotalCartPrice()
                db.clearCart()
                Toast.makeText(this, "Оплата на сумму ${totalPrice}$ прошла успешно!", Toast.LENGTH_LONG).show()
                loadCartItems()
            } else {
                Toast.makeText(this, "Корзина пуста", Toast.LENGTH_SHORT).show()
            }
        }

        loadCartItems()
    }

    override fun onResume() {
        super.onResume()
        loadCartItems()
    }

    private fun loadCartItems() {
        val cartList = db.getCartItems()
        cartAdapter.updateCartItems(cartList)

        if (cartList.isNotEmpty()) {
            val totalPrice = db.getTotalCartPrice()
            totalPriceText.text = "Общая сумма: ${totalPrice}$"
            totalPriceText.visibility = View.VISIBLE
            emptyCartText.visibility = View.GONE
        } else {
            totalPriceText.visibility = View.GONE
            emptyCartText.visibility = View.VISIBLE
        }
    }
}