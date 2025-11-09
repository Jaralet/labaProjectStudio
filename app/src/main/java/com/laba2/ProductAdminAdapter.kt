package com.laba2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdminAdapter(
    private var products: List<Item>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<ProductAdminAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.product_title_text)
        val priceText: TextView = itemView.findViewById(R.id.product_price_text)
        val deleteButton: ImageButton = itemView.findViewById(R.id.btn_delete_product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_admin_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.titleText.text = product.title
        holder.priceText.text = "Цена: ${product.price}$"
        holder.deleteButton.setOnClickListener {
            onDeleteClick(product.id)
        }
    }

    override fun getItemCount() = products.size

    fun updateProducts(newProducts: List<Item>) {
        products = newProducts
        notifyDataSetChanged()
    }
}