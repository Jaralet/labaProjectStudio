package com.laba2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AuthActivity : AppCompatActivity() {

    companion object {
        private const val ADMIN_LOGIN = "admin"
        private const val ADMIN_PASSWORD = "admin123" // Более безопасный пароль
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPass: EditText = findViewById(R.id.user_pass_auth)
        val button: Button = findViewById(R.id.button_auth)
        val linkToReg: TextView = findViewById(R.id.link_to_reg)

        linkToReg.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        button.setOnClickListener {
            val loginInput = userLogin.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if (loginInput.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Проверка на администратора
            if (loginInput == ADMIN_LOGIN && pass == ADMIN_PASSWORD) {
                Toast.makeText(this, "Вход как администратор", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AdminDashboardActivity::class.java)
                startActivity(intent)
                finish()
                return@setOnClickListener
            }

            // Проверка обычного пользователя
            val db = DbHelper(this, null)
            val isAuth = db.getUser(loginInput, pass)

            if (isAuth) {
                Toast.makeText(this, "Вы успешно авторизованы!", Toast.LENGTH_LONG).show()
                val intent = Intent(this, ItemsListActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Ошибка авторизации", Toast.LENGTH_LONG).show()
            }
        }
    }
}