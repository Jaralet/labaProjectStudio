package com.laba2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "shop.db"
        private const val TABLE_USERS = "users"
        private const val TABLE_PRODUCTS = "products"
        private const val TABLE_CART = "cart"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val userQuery = "CREATE TABLE $TABLE_USERS (id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT UNIQUE, email TEXT UNIQUE, phone TEXT UNIQUE, pass TEXT)"
        db!!.execSQL(userQuery)

        val productQuery = "CREATE TABLE $TABLE_PRODUCTS (id INTEGER PRIMARY KEY AUTOINCREMENT, image TEXT, title TEXT, desc TEXT, text TEXT, price INT)"
        db.execSQL(productQuery)

        val cartQuery = "CREATE TABLE $TABLE_CART (id INTEGER PRIMARY KEY AUTOINCREMENT, product_id INTEGER, quantity INTEGER DEFAULT 1)"
        db.execSQL(cartQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        onCreate(db)
    }

    // --- Функции для пользователей ---
    fun addUser(user: User): Boolean {
        val values = ContentValues().apply {
            put("login", user.login)
            put("email", user.email)
            put("phone", user.phone)
            put("pass", user.pass)
        }
        val db = this.writableDatabase
        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result != -1L
    }

    fun getUser(loginOrEmailOrPhone: String, pass: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE (login = ? OR email = ? OR phone = ?) AND pass = ?"
        val cursor = db.rawQuery(query, arrayOf(loginOrEmailOrPhone, loginOrEmailOrPhone, loginOrEmailOrPhone, pass))
        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists
    }

    fun getAllUsers(): ArrayList<User> {
        val userList = ArrayList<User>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE login != 'admin'", null)

        if (cursor.moveToFirst()) {
            do {
                userList.add(
                    User(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("login")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("pass"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }

    fun deleteUser(login: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_USERS, "login = ?", arrayOf(login))
        db.close()
        return result > 0
    }

    // --- Функции для товаров ---
    fun addProduct(item: Item): Boolean {
        val values = ContentValues().apply {
            put("image", item.image)
            put("title", item.title)
            put("desc", item.desc)
            put("text", item.text)
            put("price", item.price)
        }
        val db = this.writableDatabase
        val result = db.insert(TABLE_PRODUCTS, null, values)
        db.close()
        return result != -1L
    }

    fun getAllProducts(): ArrayList<Item> {
        val productList = ArrayList<Item>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PRODUCTS", null)

        if (cursor.moveToFirst()) {
            do {
                productList.add(
                    Item(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("desc")),
                        cursor.getString(cursor.getColumnIndexOrThrow("text")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("price"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return productList
    }

    fun deleteProduct(id: Int): Boolean {
        val db = this.writableDatabase
        // Сначала удаляем из корзины (каскадное удаление)
        db.delete(TABLE_CART, "product_id = ?", arrayOf(id.toString()))
        // Затем удаляем товар
        val result = db.delete(TABLE_PRODUCTS, "id = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }

    // --- Функции для корзины ---
    fun addToCart(productId: Int): Boolean {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CART WHERE product_id = ?", arrayOf(productId.toString()))

        var result: Long = -1
        if (cursor.moveToFirst()) {
            val currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))
            val values = ContentValues().apply {
                put("quantity", currentQuantity + 1)
            }
            result = db.update(TABLE_CART, values, "product_id = ?", arrayOf(productId.toString())).toLong()
        } else {
            val values = ContentValues().apply {
                put("product_id", productId)
                put("quantity", 1)
            }
            result = db.insert(TABLE_CART, null, values)
        }
        cursor.close()
        db.close()
        return result != -1L
    }

    fun getCartItems(): ArrayList<CartItem> {
        val cartList = ArrayList<CartItem>()
        val db = this.readableDatabase
        val query = """
            SELECT c.id, c.product_id, c.quantity, p.title, p.image, p.price 
            FROM $TABLE_CART c 
            JOIN $TABLE_PRODUCTS p ON c.product_id = p.id
        """

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                cartList.add(
                    CartItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("product_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return cartList
    }

    fun removeFromCart(cartItemId: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_CART, "id = ?", arrayOf(cartItemId.toString()))
        db.close()
        return result > 0
    }

    fun clearCart(): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_CART, null, null)
        db.close()
        return result > 0
    }

    fun getTotalCartPrice(): Int {
        val cartItems = getCartItems()
        return cartItems.sumOf { it.productPrice * it.quantity }
    }

    // Проверка существования пользователя
    fun isUserExists(login: String, email: String, phone: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE login = ? OR email = ? OR phone = ?"
        val cursor = db.rawQuery(query, arrayOf(login, email, phone))
        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists
    }
    fun updateCartItemQuantity(cartItemId: Int, newQuantity: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("quantity", newQuantity)
        }
        val result = db.update("cart", values, "id = ?", arrayOf(cartItemId.toString()))
        db.close()
        return result > 0
    }

    fun incrementCartItem(cartItemId: Int): Boolean {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT quantity FROM cart WHERE id = ?", arrayOf(cartItemId.toString()))

        var success = false
        if (cursor.moveToFirst()) {
            val currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))
            val values = ContentValues().apply {
                put("quantity", currentQuantity + 1)
            }
            success = db.update("cart", values, "id = ?", arrayOf(cartItemId.toString())) > 0
        }
        cursor.close()
        db.close()
        return success
    }

    fun decrementCartItem(cartItemId: Int): Boolean {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT quantity FROM cart WHERE id = ?", arrayOf(cartItemId.toString()))

        var success = false
        if (cursor.moveToFirst()) {
            val currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))
            if (currentQuantity > 1) {
                val values = ContentValues().apply {
                    put("quantity", currentQuantity - 1)
                }
                success = db.update("cart", values, "id = ?", arrayOf(cartItemId.toString())) > 0
            } else {
                // Если количество = 1, то удаляем товар
                success = db.delete("cart", "id = ?", arrayOf(cartItemId.toString())) > 0
            }
        }
        cursor.close()
        db.close()
        return success
    }
}