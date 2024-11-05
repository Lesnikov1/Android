package ru.netology.kotlinandroid.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.kotlinandroid.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var nextId = 1L
    private var posts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            publisher = "21 мая в 18:36",
            likedByMe = false
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу.",
            publisher = "21 мая в 18:35",
            likes = 99,
            views = 20,
            shares = 80,
            likedByMe = false,
            video = "https://www.youtube.com/watch?v=HmjKmoct3Ws"
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "222 → http://netolo.gy/fyb",
            publisher = "21 мая в 18:35",
            likes = 99,
            views = 30,
            shares = 80,
            likedByMe = false
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "444 → http://netolo.gy/fyb",
            publisher = "21 мая в 18:35",
            likes = 99,
            views = 40,
            shares = 80,
            likedByMe = false
        )
    )


    private val data = MutableLiveData(posts)


    override fun getAll(): LiveData<List<Post>> = data


    override fun like(id: Long) {
        posts = posts.map {
            if (it.id != id) it else (it.copy(
                likedByMe = !it.likedByMe,
                likes = it.likes + if (it.likedByMe) -1 else 1
            ))
        }
        data.value = posts
    }

    override fun share(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                sharedByMe = !it.sharedByMe,
                shares = it.shares + if (it.sharedByMe) -10 else 10
            )
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(post.copy(id = nextId++)) + posts
        } else posts = posts.map { if (it.id != post.id) it else it.copy(content = post.content) }
        data.value = posts
    }
}
