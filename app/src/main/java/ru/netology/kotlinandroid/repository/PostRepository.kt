package ru.netology.kotlinandroid.repository

import androidx.lifecycle.LiveData
import ru.netology.kotlinandroid.dto.Post

interface PostRepository {
    fun share(id: Long)
    fun getAll(): LiveData<List<Post>>
    fun like(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
}