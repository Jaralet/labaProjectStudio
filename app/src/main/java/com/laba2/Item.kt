package com.laba2

// Класс для описания товара
class Item(
    val id: Int,
    val image: String, // Будем использовать имя drawable-ресурса
    val title: String,
    val desc: String, // Краткое описание
    val text: String, // Полное описание
    val price: Int
)