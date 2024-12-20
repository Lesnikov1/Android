package ru.netology.kotlinandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.kotlinandroid.dto.Post
import ru.netology.kotlinandroid.repository.PostRepository
import ru.netology.kotlinandroid.repository.PostRepositoryFilesImpl
import ru.netology.kotlinandroid.repository.PostRepositoryInMemoryImpl
import ru.netology.kotlinandroid.repository.PostRepositorySharePrefsImpl

private val empty = Post(
    id = 0L,
    author = "",
    content = "",
    publisher = "",
    likedByMe = false,
    video = null
)

class PostViewModel(application: Application ) : AndroidViewModel(application) {
    val edited = MutableLiveData(empty)
    private val repository: PostRepository = PostRepositoryFilesImpl(application)
    val data = repository.getAll()


    fun applyChangesAndSave(newText: String) {
        edited.value?.let {
            val text = newText.trim()
            if (text != it.content) {
                repository.save(it.copy(content = text))
            }
        }
        clearEdit()
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun clearEdit() {
        edited.value = empty
    }

    fun like(id: Long) = repository.like(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }
}