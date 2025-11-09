package com.laba2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private var users: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val loginText: TextView = itemView.findViewById(R.id.user_login_text)
        val emailText: TextView = itemView.findViewById(R.id.user_email_text)
        val phoneText: TextView = itemView.findViewById(R.id.user_phone_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.loginText.text = "Login: ${user.login}"
        holder.emailText.text = "Email: ${user.email}"
        holder.phoneText.text = "Phone: ${user.phone}"
    }

    override fun getItemCount() = users.size

    fun updateUsers(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()
    }
}