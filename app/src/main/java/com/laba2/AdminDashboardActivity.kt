package com.laba2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val btnManageUsers: Button = findViewById(R.id.btn_manage_users)
        val btnManageProducts: Button = findViewById(R.id.btn_manage_products)

        btnManageUsers.setOnClickListener {
            startActivity(Intent(this, UsersActivity::class.java))
        }

        btnManageProducts.setOnClickListener {
            startActivity(Intent(this, ProductsActivity::class.java))
        }
    }
}