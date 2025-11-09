package com.laba2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userLogin: EditText = findViewById(R.id.user_login_reg)
        val userEmail: EditText = findViewById(R.id.user_email_reg)
        val userPhone: EditText = findViewById(R.id.user_phone_reg)
        val userPass: EditText = findViewById(R.id.user_pass_reg)
        val button: Button = findViewById(R.id.button_reg)
        val linkToAuth: TextView = findViewById(R.id.link_to_auth)

        linkToAuth.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
        }

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val phone = userPhone.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if (login.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Некорректный email", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (pass.length < 6) {
                Toast.makeText(this, "Пароль должен содержать минимум 6 символов", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val db = DbHelper(this, null)

            // Проверка на существующего пользователя
            if (db.isUserExists(login, email, phone)) {
                Toast.makeText(this, "Пользователь с таким логином, email или телефоном уже существует", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val user = User(0, login, email, phone, pass)
            val success = db.addUser(user)

            if (success) {
                Toast.makeText(this, "Пользователь $login успешно зарегистрирован", Toast.LENGTH_LONG).show()

                userLogin.text.clear()
                userEmail.text.clear()
                userPhone.text.clear()
                userPass.text.clear()

                // Автоматический переход на авторизацию
                startActivity(Intent(this, AuthActivity::class.java))
            } else {
                Toast.makeText(this, "Ошибка при регистрации пользователя", Toast.LENGTH_LONG).show()
            }
        }
    }
}