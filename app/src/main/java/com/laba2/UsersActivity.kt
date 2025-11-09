package com.laba2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UsersActivity : AppCompatActivity() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var db: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        db = DbHelper(this, null)
        val usersRecyclerView: RecyclerView = findViewById(R.id.users_recycler_view)
        val deleteLoginEditText: EditText = findViewById(R.id.edit_text_delete_login)
        val deleteButton: Button = findViewById(R.id.btn_delete_user)

        userAdapter = UserAdapter(ArrayList())
        usersRecyclerView.layoutManager = LinearLayoutManager(this)
        usersRecyclerView.adapter = userAdapter

        loadUsers()

        deleteButton.setOnClickListener {
            val loginToDelete = deleteLoginEditText.text.toString().trim()
            if (loginToDelete.isNotEmpty()) {
                db.deleteUser(loginToDelete)
                Toast.makeText(this, "Пользователь $loginToDelete удален", Toast.LENGTH_SHORT).show()
                deleteLoginEditText.text.clear()
                loadUsers() // Обновляем список
            } else {
                Toast.makeText(this, "Введите логин", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadUsers() {
        val userList = db.getAllUsers()
        userAdapter.updateUsers(userList)
    }
}