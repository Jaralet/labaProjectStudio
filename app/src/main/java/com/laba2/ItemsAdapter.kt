package com.laba2

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ItemsAdapter(var items: List<Item>, var context: Context) : RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.item_list_image)
        val title: TextView = view.findViewById(R.id.item_list_title)
        val desc: TextView = view.findViewById(R.id.item_list_desc)
        val price: TextView = view.findViewById(R.id.item_list_price)
        val btn: Button = view.findViewById(R.id.item_list_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.desc.text = item.desc
        holder.price.text = "${item.price}$"

        // Улучшенная загрузка изображения
        loadImage(item.image, holder.image)

        holder.btn.setOnClickListener {
            val intent = Intent(context, ItemDetailActivity::class.java)
            intent.putExtra("item_id", item.id)
            intent.putExtra("item_title", item.title)
            intent.putExtra("item_image", item.image)
            intent.putExtra("item_text", item.text)
            intent.putExtra("item_price", item.price)
            context.startActivity(intent)
        }
    }

    private fun loadImage(imagePath: String, imageView: ImageView) {
        try {
            if (imagePath.startsWith("http")) {
                Glide.with(context)
                    .load(imagePath)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(imageView)
            } else {
                val imageId = context.resources.getIdentifier(imagePath, "drawable", context.packageName)
                if (imageId != 0) {
                    imageView.setImageResource(imageId)
                } else {
                    imageView.setImageResource(R.drawable.ic_baseline_image_24)
                }
            }
        } catch (e: Exception) {
            Log.e("ItemsAdapter", "Error loading image: ${e.message}")
            imageView.setImageResource(R.drawable.ic_baseline_error_24)
        }
    }
}