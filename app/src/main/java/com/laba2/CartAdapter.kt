package com.laba2

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class CartAdapter(
    private var cartItems: List<CartItem>,
    private val onRemoveClick: (Int) -> Unit,
    private val onQuantityChange: (Int, Int) -> Unit  // Новый callback для изменения количества
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.cart_item_image)
        val titleText: TextView = itemView.findViewById(R.id.cart_item_title)
        val priceText: TextView = itemView.findViewById(R.id.cart_item_price)
        val quantityText: TextView = itemView.findViewById(R.id.cart_item_quantity)
        val totalPriceText: TextView = itemView.findViewById(R.id.cart_item_total_price)
        val removeButton: ImageButton = itemView.findViewById(R.id.btn_remove_from_cart)
        val increaseButton: ImageButton = itemView.findViewById(R.id.btn_increase_quantity)
        val decreaseButton: ImageButton = itemView.findViewById(R.id.btn_decrease_quantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]

        holder.titleText.text = cartItem.productTitle
        holder.priceText.text = "${cartItem.productPrice}$ за шт."
        holder.quantityText.text = cartItem.quantity.toString()
        holder.totalPriceText.text = "Всего: ${cartItem.productPrice * cartItem.quantity}$"

        // Загрузка изображения
        loadImage(cartItem.productImage, holder.image, holder.itemView.context)

        // Удаление товара
        holder.removeButton.setOnClickListener {
            onRemoveClick(cartItem.id)
        }

        // Увеличение количества
        holder.increaseButton.setOnClickListener {
            val newQuantity = cartItem.quantity + 1
            onQuantityChange(cartItem.id, newQuantity)
        }

        // Уменьшение количества
        holder.decreaseButton.setOnClickListener {
            val newQuantity = cartItem.quantity - 1
            if (newQuantity >= 1) {
                onQuantityChange(cartItem.id, newQuantity)
            }
        }
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateCartItems(newCartItems: List<CartItem>) {
        cartItems = newCartItems
        notifyDataSetChanged()
    }

    private fun loadImage(imagePath: String, imageView: ImageView, context: android.content.Context) {
        try {
            if (imagePath.startsWith("http")) {
                Glide.with(context)
                    .load(imagePath)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(imageView)
            } else {
                val imageId = context.resources.getIdentifier(
                    imagePath,
                    "drawable",
                    context.packageName
                )
                if (imageId != 0) {
                    imageView.setImageResource(imageId)
                } else {
                    imageView.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            }
        } catch (e: Exception) {
            Log.e("CartAdapter", "Error loading image: ${e.message}")
            imageView.setImageResource(android.R.drawable.ic_menu_report_image)
        }
    }
}