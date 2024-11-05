package ru.netology.kotlinandroid.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val publisher: String,
    val likes: Int = 0,
    val views: Int = 0,
    val shares: Int = 0,
    val likedByMe: Boolean = false,
    val sharedByMe: Boolean = false,
    val video: String? = null
)